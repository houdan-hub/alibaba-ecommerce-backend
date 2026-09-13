package com.alibaba.internship.mapper;

import com.alibaba.internship.entity.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("SELECT id, goods_id, goods_name, original_price, discount_price, master_img, " +
            "intro, address, begin_time, end_time, postage, inventory, sale_volume, " +
            "video_url, version, create_time, update_time " +
            "FROM goods WHERE goods_id = #{goodsId}")
    Goods findByGoodsId(String goodsId);

    @Select("SELECT id, goods_id, goods_name, original_price, discount_price, master_img, " +
            "intro, postage, inventory, sale_volume " +
            "FROM goods ORDER BY sale_volume DESC LIMIT #{offset}, #{size}")
    List<Goods> findPage(@Param("offset") int offset, @Param("size") int size);

    @Insert("INSERT INTO goods(goods_id, goods_name, original_price, discount_price, master_img, " +
            "intro, address, begin_time, end_time, postage, inventory, sale_volume, video_url) " +
            "VALUES(#{goodsId}, #{goodsName}, #{originalPrice}, #{discountPrice}, #{masterImg}, " +
            "#{intro}, #{address}, #{beginTime}, #{endTime}, #{postage}, #{inventory}, " +
            "#{saleVolume}, #{videoUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Goods goods);

    /**
     * Deduct inventory with optimistic locking.
     * Only succeeds when the version still matches what we read.
     * Returns affected row count — 0 means concurrent update conflict.
     */
    @Update("UPDATE goods SET inventory = inventory - #{num}, " +
            "sale_volume = sale_volume + #{num}, version = version + 1 " +
            "WHERE goods_id = #{goodsId} AND inventory >= #{num} AND version = #{version}")
    int deductInventory(@Param("goodsId") String goodsId,
                        @Param("num") int num,
                        @Param("version") int version);

    /** Restore stock when an order is cancelled. */
    @Update("UPDATE goods SET inventory = inventory + #{num}, " +
            "sale_volume = sale_volume - #{num} WHERE goods_id = #{goodsId}")
    int restoreInventory(@Param("goodsId") String goodsId, @Param("num") int num);
}
