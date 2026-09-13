package com.alibaba.internship.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Request body for creating a new order.
 */
public class OrderCreateRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "goodsId is required")
    private String goodsId;

    @NotBlank(message = "addressId is required")
    private String addressId;

    @Min(value = 1, message = "purchaseNum must be >= 1")
    private Integer purchaseNum = 1;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getGoodsId() { return goodsId; }
    public void setGoodsId(String goodsId) { this.goodsId = goodsId; }

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }

    public Integer getPurchaseNum() { return purchaseNum; }
    public void setPurchaseNum(Integer purchaseNum) { this.purchaseNum = purchaseNum; }
}
