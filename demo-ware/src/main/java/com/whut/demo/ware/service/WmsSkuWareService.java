package com.whut.demo.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.demo.common.to.OrderTo;
import com.whut.demo.common.to.StockLockedTaskTo;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.ware.entity.WmsSkuWareEntity;
import com.whut.demo.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
public interface WmsSkuWareService extends IService<WmsSkuWareEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Long> orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTaskTo taskTo);

    void unlockStock(OrderTo orderTo);
}

