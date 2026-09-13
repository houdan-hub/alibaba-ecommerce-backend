package com.alibaba.internship.service.impl;

import com.alibaba.internship.common.CommonException;
import com.alibaba.internship.common.ResultCode;
import com.alibaba.internship.entity.Goods;
import com.alibaba.internship.mapper.GoodsMapper;
import com.alibaba.internship.service.GoodsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Goods service with Redis Cache-Aside pattern.
 *
 * <p>Read path:  try Redis → miss → query MySQL → write Redis with TTL → return.
 * Write path:   update MySQL → delete (evict) Redis key.
 *
 * <p>Cache-Aside (also called lazy loading) is the simplest and most
 * common caching strategy: the application decides when to load and
 * invalidate cache entries, keeping Redis as a look-aside accelerator.</p>
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.redis.goods-cache-prefix:goods:}")
    private String cachePrefix;

    @Value("${app.redis.goods-cache-ttl:300}")
    private long cacheTtl;

    @Override
    public Goods getByGoodsId(String goodsId) {
        String key = cachePrefix + goodsId;

        // 1. try cache
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            try {
                log.debug("cache hit for goodsId={}", goodsId);
                return objectMapper.readValue(cached, Goods.class);
            } catch (JsonProcessingException e) {
                log.warn("failed to deserialize cache, falling back to DB", e);
            }
        }

        // 2. cache miss — query DB
        log.debug("cache miss for goodsId={}, querying DB", goodsId);
        Goods goods = goodsMapper.findByGoodsId(goodsId);
        if (goods == null) {
            throw new CommonException(ResultCode.GOODS_NOT_FOUND);
        }

        // 3. write back to cache with TTL
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(goods),
                    cacheTtl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.warn("failed to write cache", e);
        }
        return goods;
    }

    @Override
    public List<Goods> getPage(int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        return goodsMapper.findPage(offset, size);
    }

    @Override
    public Goods create(Goods goods) {
        if (goods.getGoodsId() == null || goods.getGoodsId().isBlank()) {
            goods.setGoodsId("G" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        }
        goodsMapper.insert(goods);
        return goods;
    }

    @Override
    public boolean deductInventory(String goodsId, int num) {
        Goods goods = goodsMapper.findByGoodsId(goodsId);
        if (goods == null) {
            throw new CommonException(ResultCode.GOODS_NOT_FOUND);
        }
        if (goods.getInventory() < num) {
            return false;
        }
        int rows = goodsMapper.deductInventory(goodsId, num, goods.getVersion());
        if (rows == 0) {
            // optimistic-lock conflict or concurrent deduction — caller should retry
            log.warn("inventory deduction conflict for goodsId={}", goodsId);
            return false;
        }
        // invalidate cache so subsequent reads see fresh stock
        evictCache(goodsId);
        return true;
    }

    @Override
    public void restoreInventory(String goodsId, int num) {
        goodsMapper.restoreInventory(goodsId, num);
        evictCache(goodsId);
    }

    @Override
    public void evictCache(String goodsId) {
        redisTemplate.delete(cachePrefix + goodsId);
    }
}
