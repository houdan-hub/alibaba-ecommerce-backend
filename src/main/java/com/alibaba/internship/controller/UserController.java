package com.alibaba.internship.controller;

import com.alibaba.internship.common.Result;
import com.alibaba.internship.entity.User;
import com.alibaba.internship.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public Result<User> getById(@PathVariable String userId) {
        return Result.success(userService.getByUserId(userId));
    }

    @GetMapping
    public Result<List<User>> getPage(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return Result.success(userService.getPage(page, size));
    }

    @PostMapping
    public Result<User> create(@RequestBody User user) {
        return Result.success(userService.create(user));
    }

    @PutMapping
    public Result<User> update(@RequestBody User user) {
        return Result.success(userService.update(user));
    }

    @DeleteMapping("/{userId}")
    public Result<Void> delete(@PathVariable String userId) {
        userService.delete(userId);
        return Result.success();
    }
}
