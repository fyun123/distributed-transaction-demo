package com.whut.demo.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.ware.entity.WmsOrderTaskEntity;

import java.util.Map;

/**
 * 订单任务
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
public interface WmsOrderTaskService extends IService<WmsOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

