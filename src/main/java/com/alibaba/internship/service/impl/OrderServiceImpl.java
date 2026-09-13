package com.alibaba.internship.service.impl;

import com.alibaba.internship.common.CommonException;
import com.alibaba.internship.common.ResultCode;
import com.alibaba.internship.dto.OrderCreateRequest;
import com.alibaba.internship.entity.Address;
import com.alibaba.internship.entity.Goods;
import com.alibaba.internship.entity.Order;
import com.alibaba.internship.entity.User;
import com.alibaba.internship.mapper.OrderMapper;
import com.alibaba.internship.mq.OrderTimeoutProducer;
import com.alibaba.internship.service.AddressService;
import com.alibaba.internship.service.GoodsService;
import com.alibaba.internship.service.OrderService;
import com.alibaba.internship.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Order service — the core business flow.
 *
 * <p>Create order flow:</p>
 * <ol>
 *   <li>Validate user, goods, and address exist.</li>
 *   <li>Deduct inventory with optimistic locking (retry once on conflict).</li>
 *   <li>Persist the order in PENDING_PAY state.</li>
 *   <li>Send a delayed message to RabbitMQ — if unpaid after TTL,
 *       the consumer cancels the order and restores stock.</li>
 * </ol>
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderTimeoutProducer timeoutProducer;

    @Override
    @Transactional
    public Order create(OrderCreateRequest request) {
        // 1. validate references
        User user = userService.getByUserId(request.getUserId());
        Goods goods = goodsService.getByGoodsId(request.getGoodsId());
        Address address = addressService.getByAddressId(request.getAddressId());

        int num = request.getPurchaseNum();

        // 2. deduct inventory — retry once on optimistic-lock conflict
        boolean deducted = goodsService.deductInventory(request.getGoodsId(), num);
        if (!deducted) {
            // single retry
            deducted = goodsService.deductInventory(request.getGoodsId(), num);
        }
        if (!deducted) {
            throw new CommonException(ResultCode.GOODS_OUT_OF_STOCK);
        }

        // 3. build & persist order
        Order order = new Order();
        order.setOrderId("O" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        order.setUserId(request.getUserId());
        order.setGoodsId(request.getGoodsId());
        order.setAddressId(request.getAddressId());
        order.setPurchaseNum(BigDecimal.valueOf(num));
        order.setOrderState("PENDING_PAY");
        // total = discount_price * num + postage
        BigDecimal total = goods.getDiscountPrice()
                .multiply(BigDecimal.valueOf(num))
                .add(goods.getPostage());
        order.setTotalMoney(total);

        orderMapper.insert(order);
        log.info("order created: orderId={}, userId={}, goodsId={}, total={}",
                order.getOrderId(), user.getUserId(), goods.getGoodsId(), total);

        // 4. send delayed-cancel message (if not paid within TTL, auto-cancel)
        timeoutProducer.send(order.getOrderId());

        return order;
    }

    @Override
    public Order getByOrderId(String orderId) {
        Order order = orderMapper.findByOrderId(orderId);
        if (order == null) {
            throw new CommonException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public List<Order> getByUserId(String userId, int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        return orderMapper.findByUserId(userId, offset, size);
    }

    @Override
    @Transactional
    public Order pay(String orderId, String paykey) {
        Order order = getByOrderId(orderId);
        if (!"PENDING_PAY".equals(order.getOrderState())) {
            throw new CommonException(ResultCode.ORDER_ALREADY_PAID);
        }
        int rows = orderMapper.payOrder(orderId, "PAID", paykey);
        if (rows == 0) {
            throw new CommonException(ResultCode.ORDER_ALREADY_PAID);
        }
        // Note: the RabbitMQ timeout message will still fire, but the consumer
        // checks state — PAID orders are not cancelled. This is intentional
        // and avoids needing to track/remove pending MQ messages.
        return orderMapper.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public void cancel(String orderId) {
        Order order = getByOrderId(orderId);
        if (!"PENDING_PAY".equals(order.getOrderState())) {
            throw new CommonException(ResultCode.ORDER_ALREADY_PAID);
        }
        int rows = orderMapper.cancelOrder(orderId);
        if (rows > 0) {
            goodsService.restoreInventory(order.getGoodsId(), order.getPurchaseNum().intValue());
        }
    }

    @Override
    @Transactional
    public void handleTimeoutCancel(String orderId) {
        Order order = orderMapper.findByOrderId(orderId);
        if (order == null) {
            log.warn("timeout cancel: order {} not found, skipping", orderId);
            return;
        }
        if (!"PENDING_PAY".equals(order.getOrderState())) {
            // already paid or cancelled — nothing to do
            log.info("timeout cancel: order {} is in state {}, skipping", orderId, order.getOrderState());
            return;
        }
        int rows = orderMapper.cancelOrder(orderId);
        if (rows > 0) {
            log.info("order {} auto-cancelled due to payment timeout", orderId);
            goodsService.restoreInventory(order.getGoodsId(), order.getPurchaseNum().intValue());
        }
    }
}
