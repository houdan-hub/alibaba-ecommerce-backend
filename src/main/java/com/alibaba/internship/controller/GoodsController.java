package com.alibaba.internship.controller;

import com.alibaba.internship.common.Result;
import com.alibaba.internship.entity.Goods;
import com.alibaba.internship.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * Get goods detail — served from Redis cache when available,
     * falling back to MySQL and writing back to cache (Cache-Aside).
     */
    @GetMapping("/{goodsId}")
    public Result<Goods> getById(@PathVariable String goodsId) {
        return Result.success(goodsService.getByGoodsId(goodsId));
    }

    @GetMapping
    public Result<List<Goods>> getPage(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return Result.success(goodsService.getPage(page, size));
    }

    @PostMapping
    public Result<Goods> create(@RequestBody Goods goods) {
        return Result.success(goodsService.create(goods));
    }
}
