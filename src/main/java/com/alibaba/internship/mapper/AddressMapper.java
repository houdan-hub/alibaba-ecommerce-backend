package com.alibaba.internship.mapper;

import com.alibaba.internship.entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {

    @Select("SELECT id, address_id, user_id, name, phone, detailed_address, state, " +
            "province_id, city_id, area_id, completed_address, create_time, update_time " +
            "FROM address WHERE address_id = #{addressId}")
    Address findByAddressId(String addressId);

    @Select("SELECT id, address_id, user_id, name, phone, detailed_address, state, " +
            "province_id, city_id, area_id, completed_address, create_time, update_time " +
            "FROM address WHERE user_id = #{userId} ORDER BY state DESC, id")
    List<Address> findByUserId(String userId);

    @Insert("INSERT INTO address(address_id, user_id, name, phone, detailed_address, state, " +
            "province_id, city_id, area_id, completed_address) " +
            "VALUES(#{addressId}, #{userId}, #{name}, #{phone}, #{detailedAddress}, #{state}, " +
            "#{provinceId}, #{cityId}, #{areaId}, #{completedAddress})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Address address);

    @Update("UPDATE address SET name=#{name}, phone=#{phone}, " +
            "detailed_address=#{detailedAddress}, completed_address=#{completedAddress} " +
            "WHERE address_id=#{addressId}")
    int update(Address address);

    @Delete("DELETE FROM address WHERE address_id = #{addressId}")
    int deleteByAddressId(String addressId);
}
