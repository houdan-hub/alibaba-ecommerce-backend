package com.alibaba.internship.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Goods entity — maps to the `goods` table.
 * The `version` field supports optimistic locking for inventory deduction.
 */
public class Goods {

    private Long id;
    private String goodsId;
    private String goodsName;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private String masterImg;
    private String intro;
    private String address;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private BigDecimal postage;
    private Integer inventory;
    private Integer saleVolume;
    private String videoUrl;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGoodsId() { return goodsId; }
    public void setGoodsId(String goodsId) { this.goodsId = goodsId; }

    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; }

    public String getMasterImg() { return masterImg; }
    public void setMasterImg(String masterImg) { this.masterImg = masterImg; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getBeginTime() { return beginTime; }
    public void setBeginTime(LocalDateTime beginTime) { this.beginTime = beginTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BigDecimal getPostage() { return postage; }
    public void setPostage(BigDecimal postage) { this.postage = postage; }

    public Integer getInventory() { return inventory; }
    public void setInventory(Integer inventory) { this.inventory = inventory; }

    public Integer getSaleVolume() { return saleVolume; }
    public void setSaleVolume(Integer saleVolume) { this.saleVolume = saleVolume; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
