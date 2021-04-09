package com.whut.demo.ware.vo;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 订单号
     */
    private String bizOrderId;
    /**
     * 订单状态【0-新建；1-已完成；2-已取消】
     */
    private Integer orderStatus;
    /**
     * 支付金额
     */
    private BigDecimal payment;
}
