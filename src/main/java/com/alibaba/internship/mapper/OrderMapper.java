package com.alibaba.internship.mapper;

import com.alibaba.internship.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT id, order_id, user_id, goods_id, purchase_num, address_id, " +
            "order_state, paykey, total_money, cancel_time, create_time, update_time " +
            "FROM orders WHERE order_id = #{orderId}")
    Order findByOrderId(String orderId);

    @Select("SELECT id, order_id, user_id, goods_id, purchase_num, address_id, " +
            "order_state, total_money, create_time " +
            "FROM orders WHERE user_id = #{userId} ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{size}")
    List<Order> findByUserId(@Param("userId") String userId,
                             @Param("offset") int offset,
                             @Param("size") int size);

    @Insert("INSERT INTO orders(order_id, user_id, goods_id, purchase_num, address_id, " +
            "order_state, total_money) " +
            "VALUES(#{orderId}, #{userId}, #{goodsId}, #{purchaseNum}, #{addressId}, " +
            "#{orderState}, #{totalMoney})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Update("UPDATE orders SET order_state = #{state}, paykey = #{paykey} " +
            "WHERE order_id = #{orderId} AND order_state = 'PENDING_PAY'")
    int payOrder(@Param("orderId") String orderId,
                 @Param("state") String state,
                 @Param("paykey") String paykey);

    @Update("UPDATE orders SET order_state = 'CANCELLED', cancel_time = NOW() " +
            "WHERE order_id = #{orderId} AND order_state = 'PENDING_PAY'")
    int cancelOrder(String orderId);

    @Update("UPDATE orders SET order_state = #{state} WHERE order_id = #{orderId}")
    int updateState(@Param("orderId") String orderId, @Param("state") String state);
}
