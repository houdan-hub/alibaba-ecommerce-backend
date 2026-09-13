package com.alibaba.internship.mapper;

import com.alibaba.internship.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT id, user_id, email, password, nick_name, gender, " +
            "avatar_url, balance, paykey, create_time, update_time " +
            "FROM user WHERE user_id = #{userId}")
    User findByUserId(String userId);

    @Select("SELECT id, user_id, email, nick_name, gender, " +
            "avatar_url, balance, create_time, update_time " +
            "FROM user ORDER BY id LIMIT #{offset}, #{size}")
    List<User> findPage(@Param("offset") int offset, @Param("size") int size);

    @Insert("INSERT INTO user(user_id, email, password, nick_name, gender, avatar_url, balance, paykey) " +
            "VALUES(#{userId}, #{email}, #{password}, #{nickName}, #{gender}, #{avatarUrl}, #{balance}, #{paykey})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET nick_name=#{nickName}, gender=#{gender}, " +
            "avatar_url=#{avatarUrl}, balance=#{balance} WHERE user_id=#{userId}")
    int update(User user);

    @Delete("DELETE FROM user WHERE user_id = #{userId}")
    int deleteByUserId(String userId);
}
