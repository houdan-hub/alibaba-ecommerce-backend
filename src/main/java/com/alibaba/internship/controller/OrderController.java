package com.alibaba.internship.controller;

import com.alibaba.internship.common.Result;
import com.alibaba.internship.dto.OrderCreateRequest;
import com.alibaba.internship.entity.Order;
import com.alibaba.internship.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create a new order.
     * Triggers inventory deduction + RabbitMQ delayed auto-cancel.
     */
    @PostMapping
    public Result<Order> create(@Valid @RequestBody OrderCreateRequest request) {
        return Result.success(orderService.create(request));
    }

    @GetMapping("/{orderId}")
    public Result<Order> getById(@PathVariable String orderId) {
        return Result.success(orderService.getByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Order>> getByUserId(@PathVariable String userId,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return Result.success(orderService.getByUserId(userId, page, size));
    }

    /** Mark an unpaid order as PAID. */
    @PostMapping("/{orderId}/pay")
    public Result<Order> pay(@PathVariable String orderId,
                             @RequestBody Map<String, String> body) {
        String paykey = body.getOrDefault("paykey", "");
        return Result.success(orderService.pay(orderId, paykey));
    }

    /** User-initiated cancel of an unpaid order. */
    @PostMapping("/{orderId}/cancel")
    public Result<Void> cancel(@PathVariable String orderId) {
        orderService.cancel(orderId);
        return Result.success();
    }
}
