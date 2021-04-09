package com.whut.demo.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareSkuLockVo {

    private String bizOrderId;

    private List<OrderItemVo> locks;
}
