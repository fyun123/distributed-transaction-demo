package com.whut.demo.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.whut.demo.common.constant.OrderConstant;
import com.whut.demo.common.constant.PayConstant;
import com.whut.demo.common.exception.NoStockException;
import com.whut.demo.common.to.OrderTo;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.Query;
import com.whut.demo.common.utils.R;
import com.whut.demo.order.entity.OmsOrderItemEntity;
import com.whut.demo.order.feign.ProductFeignService;
import com.whut.demo.order.feign.WareFeignService;
import com.whut.demo.order.service.OmsOrderItemService;
import com.whut.demo.order.vo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.whut.demo.order.dao.OmsOrderDao;
import com.whut.demo.order.entity.OmsOrderEntity;
import com.whut.demo.order.service.OmsOrderService;
import org.springframework.transaction.annotation.Transactional;


@Service("omsOrderService")
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderDao, OmsOrderEntity> implements OmsOrderService {

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    OmsOrderItemService omsOrderItemService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final String USER_ORDER_TOKEN = "demo:token";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrderEntity> page = this.page(
                new Query<OmsOrderEntity>().getPage(params),
                new QueryWrapper<OmsOrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        List<OrderItemVo> currentCart = productFeignService.getCurrentCart();
        orderConfirmVo.setItems(currentCart);
        // 5. ????????????
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(USER_ORDER_TOKEN, token, 30, TimeUnit.MINUTES);
        orderConfirmVo.setOrderToken(token);
        return orderConfirmVo;
    }

    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo) {
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        responseVo.setCode(0);
        // 0 - ???????????? 1 - ????????????
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        String orderToken = submitVo.getOrderToken();
        // ?????????????????????????????????
        Long execute = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(USER_ORDER_TOKEN), orderToken);
        if (execute == 0L) {
            // ??????????????????
            responseVo.setCode(1);
        } else {
            // ??????????????????
            // 1. ????????????
            OmsOrderEntity order = new OmsOrderEntity();
            String orderSn = IdWorker.getTimeId();
            order.setBizOrderId(orderSn);
            order.setOrderStatus(OrderConstant.OrderStatusConstant.CREATE.getCode());
            order.setPayment(new BigDecimal(submitVo.getPayPrice()));
            this.save(order);
            // ???????????????
            List<OrderItemVo> currentCart = productFeignService.getCurrentCart();
            if (currentCart != null && currentCart.size() > 0) {
                List<OmsOrderItemEntity> orderItems = currentCart.stream().map(item -> {
                    OmsOrderItemEntity orderItem = new OmsOrderItemEntity();
                    BeanUtils.copyProperties(item, orderItem);
                    orderItem.setBizOrderId(orderSn);
                    orderItem.setSkuNum(item.getNum());
                    orderItem.setAmount(item.getTotalPrice());
                    return orderItem;
                }).collect(Collectors.toList());
                omsOrderItemService.saveBatch(orderItems);
                order.setOrderItemEntities(orderItems);
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                wareSkuLockVo.setBizOrderId(orderSn);
                wareSkuLockVo.setLocks(currentCart);
                R r = wareFeignService.orderLockStock(wareSkuLockVo);
                if (r.getCode() == 0) {
                    // ????????????
//                    int i = 10 / 0; //?????????????????????????????????????????????????????????
                    rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order);
                    responseVo.setOrder(order);
                } else {
                    List<Long> skuIds = r.get("skuIds", new TypeReference<List<Long>>() {
                    });
                    throw new NoStockException(skuIds);
                }
            }
        }
        return responseVo;
    }

    @Override
    public List<OmsOrderEntity> listOrder() {
        List<OmsOrderEntity> orders = this.list();
        if (orders != null && orders.size() > 0){
            for (OmsOrderEntity order : orders) {
                List<OmsOrderItemEntity> biz_order_id = omsOrderItemService.list(new QueryWrapper<OmsOrderItemEntity>().eq("biz_order_id", order.getBizOrderId()));
                order.setOrderItemEntities(biz_order_id);
            }
        }
        return orders;
    }

    @Override
    public PayRespVo pay(PayVo payVo) {
        PayRespVo vo = new PayRespVo(payVo.getBizOrderId(),PayConstant.PayRespConstant.SUCCESS.getCode());
        OmsOrderEntity order = getOne(new QueryWrapper<OmsOrderEntity>().eq("biz_order_id", payVo.getBizOrderId()));
        if (order != null){
            if (order.getOrderStatus() == 0){
                // ?????????
                if (payVo.getPayedMoney().equals(order.getPayment().toString())){
                    // ????????????0
                    // ??????????????????
                    OmsOrderEntity payedOrder = new OmsOrderEntity();
                    payedOrder.setId(order.getId());
                    payedOrder.setOrderStatus(OrderConstant.OrderStatusConstant.PAYED.getCode());
                    updateById(payedOrder);
                    // ????????????
                } else {
                    // ????????????1
                    vo.setCode(PayConstant.PayRespConstant.FAILED.getCode());
                }
            } else {
                // ????????????2
                vo.setCode(PayConstant.PayRespConstant.SECOND_PAY.getCode());
            }
        } else {
            // ???????????????3
            vo.setCode(PayConstant.PayRespConstant.ORDER_NON_EXIST.getCode());
        }
        return vo;
    }

    @Override
    public OmsOrderEntity getOrderStatus(String bizOrderId) {
        return getOne(new QueryWrapper<OmsOrderEntity>().eq("biz_order_id",bizOrderId));
    }

    @Override
    public void closeOrder(String bizOrderId) {
        // ??????????????????????????????
        OmsOrderEntity order = getOne(new QueryWrapper<OmsOrderEntity>().eq("biz_order_id", bizOrderId));
        if (order != null) {
            if (order.getOrderStatus() == OrderConstant.OrderStatusConstant.CREATE.getCode()){
                //??????????????? ????????????
                OmsOrderEntity updateOrder = new OmsOrderEntity();
                updateOrder.setId(order.getId());
                updateOrder.setOrderStatus(OrderConstant.OrderStatusConstant.CANCEL.getCode());
                this.updateById(updateOrder);
                // ??????MQ
                OrderTo orderTo = new OrderTo();
                BeanUtils.copyProperties(order,orderTo);
                rabbitTemplate.convertAndSend("order-event-exchange","order.release.other",orderTo);
            }
        }
    }

}