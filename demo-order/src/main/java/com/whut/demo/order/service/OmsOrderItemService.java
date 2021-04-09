package com.whut.demo.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.order.entity.OmsOrderItemEntity;

import java.util.Map;

/**
 * 订单项
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:25:59
 */
public interface OmsOrderItemService extends IService<OmsOrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

