package com.alibaba.internship.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order entity — maps to the `orders` table.
 *
 * <p>order_state lifecycle:
 * PENDING_PAY → PAID → SHIPPED → COMPLETED
 *            ↘ CANCELLED (timeout or user action)</p>
 */
public class Order {

    private Long id;
    private String orderId;
    private String userId;
    private String goodsId;
    private BigDecimal purchaseNum;
    private String addressId;
    private String orderState;
    private String paykey;
    private BigDecimal totalMoney;
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getGoodsId() { return goodsId; }
    public void setGoodsId(String goodsId) { this.goodsId = goodsId; }

    public BigDecimal getPurchaseNum() { return purchaseNum; }
    public void setPurchaseNum(BigDecimal purchaseNum) { this.purchaseNum = purchaseNum; }

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }

    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }

    public String getPaykey() { return paykey; }
    public void setPaykey(String paykey) { this.paykey = paykey; }

    public BigDecimal getTotalMoney() { return totalMoney; }
    public void setTotalMoney(BigDecimal totalMoney) { this.totalMoney = totalMoney; }

    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime cancelTime) { this.cancelTime = cancelTime; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
