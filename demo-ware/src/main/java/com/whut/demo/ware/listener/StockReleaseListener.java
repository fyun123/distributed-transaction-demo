package com.whut.demo.ware.listener;

import com.rabbitmq.client.Channel;
import com.whut.demo.common.to.OrderTo;
import com.whut.demo.common.to.StockLockedTaskTo;
import com.whut.demo.ware.service.WmsSkuWareService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {

    @Autowired
    WmsSkuWareService wareSkuService;

    /**
     * 库存自动解锁
     *只要解锁库存的消息的失败，需要告诉MQ解锁失败，消息不要删除，重新放回队列
     * @param taskTo
     * @param message
     *
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTaskTo taskTo, Message message, Channel channel) throws IOException {
        System.out.println("收到解锁库存的消息");
        try{
            wareSkuService.unlockStock(taskTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    @RabbitHandler
    public void handleOrderCloseRelease(OrderTo orderTo, Message message, Channel channel) throws IOException {
        System.out.println("订单关闭，准备解锁库存");
        try{
            wareSkuService.unlockStock(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
