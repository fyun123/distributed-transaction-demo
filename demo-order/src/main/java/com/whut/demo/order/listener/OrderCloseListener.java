package com.whut.demo.order.listener;

import com.rabbitmq.client.Channel;
import com.whut.demo.order.entity.OmsOrderEntity;
import com.whut.demo.order.service.OmsOrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RabbitListener(queues = "order.release.order.queue")
@Service
public class OrderCloseListener {

    @Autowired
    OmsOrderService orderService;

    @RabbitHandler
    public void listener(OmsOrderEntity orderEntity, Channel channel, Message message) throws IOException {
        System.out.println("收到过期订单，准备关单："+orderEntity.getBizOrderId());
        try{
            orderService.closeOrder(orderEntity.getBizOrderId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
