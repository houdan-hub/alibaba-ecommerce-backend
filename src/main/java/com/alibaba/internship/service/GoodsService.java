package com.alibaba.internship.service;

import com.alibaba.internship.entity.Goods;
import java.util.List;

public interface GoodsService {
    /** Get goods detail — uses Redis cache-aside pattern. */
    Goods getByGoodsId(String goodsId);

    List<Goods> getPage(int page, int size);

    Goods create(Goods goods);

    /**
     * Try to deduct inventory with optimistic locking.
     * Returns true on success, false on stock shortage or version conflict.
     */
    boolean deductInventory(String goodsId, int num);

    /** Restore stock after order cancellation. */
    void restoreInventory(String goodsId, int num);

    /** Evict goods cache after update — called by order flow. */
    void evictCache(String goodsId);
}
