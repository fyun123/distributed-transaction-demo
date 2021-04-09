package com.whut.demo.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVo {

    private Long skuId;

    private String skuName;

    private BigDecimal skuPrice;

    private Integer num;

    private BigDecimal totalPrice;

}
