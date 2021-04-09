package com.whut.demo.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.order.entity.OmsOrderEntity;
import com.whut.demo.order.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 订单
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:25:59
 */
public interface OmsOrderService extends IService<OmsOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder();

    SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo);

    List<OmsOrderEntity> listOrder();

    PayRespVo pay(PayVo payVo);

    OmsOrderEntity getOrderStatus(String bizOrderId);

    void closeOrder(String bizOrderId);
}

