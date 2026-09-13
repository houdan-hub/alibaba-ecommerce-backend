package com.alibaba.internship.service;

import com.alibaba.internship.entity.User;
import java.util.List;

public interface UserService {
    User getByUserId(String userId);
    List<User> getPage(int page, int size);
    User create(User user);
    User update(User user);
    void delete(String userId);
}
