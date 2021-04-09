package com.whut.demo.order.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class OrderConfirmVo {

    // 所有选中的购物项
    @Setter @Getter
    private List<OrderItemVo> items;

    // 订单防重令牌
    @Setter @Getter
    private String orderToken;

    public BigDecimal getPayAmount() {
        BigDecimal sum = new BigDecimal("0.00");
        if (items != null){
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getTotalPrice();
                sum = sum.add(multiply);
            }
        }
        return sum;
    }
}
