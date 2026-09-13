package com.alibaba.internship.service.impl;

import com.alibaba.internship.common.CommonException;
import com.alibaba.internship.common.ResultCode;
import com.alibaba.internship.entity.User;
import com.alibaba.internship.mapper.UserMapper;
import com.alibaba.internship.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getByUserId(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CommonException(ResultCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public List<User> getPage(int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        return userMapper.findPage(offset, size);
    }

    @Override
    public User create(User user) {
        if (user.getUserId() == null || user.getUserId().isBlank()) {
            user.setUserId("U" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        }
        userMapper.insert(user);
        return user;
    }

    @Override
    public User update(User user) {
        int rows = userMapper.update(user);
        if (rows == 0) {
            throw new CommonException(ResultCode.USER_NOT_FOUND);
        }
        return userMapper.findByUserId(user.getUserId());
    }

    @Override
    public void delete(String userId) {
        int rows = userMapper.deleteByUserId(userId);
        if (rows == 0) {
            throw new CommonException(ResultCode.USER_NOT_FOUND);
        }
    }
}
