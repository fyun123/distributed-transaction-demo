package com.whut.demo.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.whut.demo.common.constant.OrderConstant;
import com.whut.demo.common.constant.WareConstant;
import com.whut.demo.common.exception.NoStockException;
import com.whut.demo.common.to.OrderTo;
import com.whut.demo.common.to.StockLockedTaskDetailTo;
import com.whut.demo.common.to.StockLockedTaskTo;
import com.whut.demo.common.utils.R;
import com.whut.demo.ware.dao.WmsSkuWareDao;
import com.whut.demo.ware.entity.WmsOrderTaskDetailEntity;
import com.whut.demo.ware.entity.WmsOrderTaskEntity;
import com.whut.demo.ware.entity.WmsSkuWareEntity;
import com.whut.demo.ware.feign.OrderFeignService;
import com.whut.demo.ware.service.WmsOrderTaskDetailService;
import com.whut.demo.ware.service.WmsOrderTaskService;
import com.whut.demo.ware.service.WmsSkuWareService;
import com.whut.demo.ware.vo.OrderItemVo;
import com.whut.demo.ware.vo.OrderVo;
import com.whut.demo.ware.vo.WareSkuLockVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("wmsSkuWareService")
public class WmsSkuWareServiceImpl extends ServiceImpl<WmsSkuWareDao, WmsSkuWareEntity> implements WmsSkuWareService {

    @Autowired
    WmsOrderTaskService taskService;

    @Autowired
    WmsOrderTaskDetailService taskDetailService;

    @Resource
    WmsSkuWareDao skuWareDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsSkuWareEntity> page = this.page(
                new Query<WmsSkuWareEntity>().getPage(params),
                new QueryWrapper<WmsSkuWareEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public List<Long> orderLockStock(WareSkuLockVo vo) {
        List<Long> failSkuIds = new ArrayList<>();
        // 保存工作单
        WmsOrderTaskEntity task = new WmsOrderTaskEntity();
        task.setBizOrderId(vo.getBizOrderId());
        taskService.save(task);
        // 锁定库存
        List<OrderItemVo> orderItems = vo.getLocks();
        for (OrderItemVo orderItem : orderItems) {
            Long count = skuWareDao.lockStock(orderItem.getSkuId(),orderItem.getNum());
            if (count == 1){
                // 锁定成功
                // 保存工作单详情
                WmsOrderTaskDetailEntity taskDetail = new WmsOrderTaskDetailEntity();
                taskDetail.setTaskId(task.getId());
                taskDetail.setSkuId(orderItem.getSkuId());
                taskDetail.setSkuNum(orderItem.getNum());
                taskDetail.setLockStatus(WareConstant.WareTaskStatusConstant.LOCKED.getCode());
                taskDetailService.save(taskDetail);
                // 自动解锁
                // 防止回滚后找不到数据
                StockLockedTaskTo taskTo = new StockLockedTaskTo();
                taskTo.setId(task.getId());
                StockLockedTaskDetailTo taskDetailTo = new StockLockedTaskDetailTo();
                BeanUtils.copyProperties(taskDetail,taskDetailTo);
                taskTo.setDetailTo(taskDetailTo);
                // 锁定库存，发送消息到延时队列
                rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", taskTo);
            } else {
                // 锁定失败
                failSkuIds.add(orderItem.getSkuId());
            }
        }
        if (failSkuIds.size() > 0) {
            throw new NoStockException(failSkuIds);
        }
        return null;
    }

    @Transactional
    @Override
    public void unlockStock(StockLockedTaskTo taskTo) {
        Long taskDetailId = taskTo.getDetailTo().getId();
        WmsOrderTaskDetailEntity detail = taskDetailService.getById(taskDetailId);
        if (detail != null){
            // 查询订单状态，订单状态为2【已取消】，则解锁看库存
            Long taskId = taskTo.getId();
            WmsOrderTaskEntity task = taskService.getById(taskId);
            String bizOrderId = task.getBizOrderId();
            // 远程调用订单服务，获取订单状态
            R r = orderFeignService.getOrderStatus(bizOrderId);
            if (r.getCode() == 0) {
                OrderVo orderVo = r.getData(new TypeReference<OrderVo>() {
                });
                if (orderVo == null || orderVo.getOrderStatus() == OrderConstant.OrderStatusConstant.CANCEL.getCode()) {
                    // 订单不存在，或者订单已取消，都需要解锁库存
                    if (detail.getLockStatus() == WareConstant.WareTaskStatusConstant.LOCKED.getCode()){
                        //锁定状态下才需要解锁，已解锁的不用在解锁
                        unlockStock(detail.getId(),detail.getSkuId(),detail.getSkuNum());
                        System.out.println("库存解锁成功");
                    }
                }
            } else {
                throw new RuntimeException("远程调用订单服务失败");
            }
        }
    }

    private void unlockStock(Long taskDetailId, Long skuId, Integer skuNum) {
        // 解锁库存
        skuWareDao.unlockStock(skuId, skuNum);
        // 更新taskDetail状态
        WmsOrderTaskDetailEntity taskDetailEntity = new WmsOrderTaskDetailEntity();
        taskDetailEntity.setId(taskDetailId);
        taskDetailEntity.setLockStatus(WareConstant.WareTaskStatusConstant.UNLOCKED.getCode());
        taskDetailService.updateById(taskDetailEntity);
    }

    // 防止订单服务卡顿，导致订单状态消息一直改不了，库存消息优先到期，查询订单状态为新建状态，不解锁库存，并删除消息，导致库永远无法解锁
    @Transactional
    @Override
    public void unlockStock(OrderTo orderTo) {
        // 无需查询订单最新状态，能来到这，肯定更新了订单状态的
        String bizOrderId = orderTo.getBizOrderId();
        WmsOrderTaskEntity task = taskService.getOne(new QueryWrapper<WmsOrderTaskEntity>().eq("biz_order_id", bizOrderId));
        if (task != null){
            List<WmsOrderTaskDetailEntity> taskDetails = taskDetailService.list(new QueryWrapper<WmsOrderTaskDetailEntity>()
                    .eq("task_id", task.getId())
                    .eq("lock_status", WareConstant.WareTaskStatusConstant.LOCKED.getCode()));
            if (taskDetails != null && taskDetails.size() > 0){
                for (WmsOrderTaskDetailEntity taskDetail : taskDetails) {
                    unlockStock(taskDetail.getId(),taskDetail.getSkuId(),taskDetail.getSkuNum());
                    System.out.println("库存解锁成功");
                }
            }
        }
    }
}