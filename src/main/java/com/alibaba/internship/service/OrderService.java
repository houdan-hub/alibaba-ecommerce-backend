package com.alibaba.internship.service;

import com.alibaba.internship.dto.OrderCreateRequest;
import com.alibaba.internship.entity.Order;
import java.util.List;

public interface OrderService {
    /**
     * Create an order: validate user/goods/address, deduct stock,
     * persist order, then send a delayed message to RabbitMQ for
     * auto-cancellation if unpaid after TTL.
     */
    Order create(OrderCreateRequest request);

    Order getByOrderId(String orderId);

    List<Order> getByUserId(String userId, int page, int size);

    /** Mark order as PAID (also removes it from timeout-cancel scope). */
    Order pay(String orderId, String paykey);

    /** Cancel an unpaid order and restore stock. */
    void cancel(String orderId);

    /** Called by RabbitMQ consumer when timeout fires. */
    void handleTimeoutCancel(String orderId);
}
