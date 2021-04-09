package com.whut.demo.product.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public class CartItemVo {
    @Getter @Setter
    private Boolean check = true;
    @Getter @Setter
    private Long skuId;
    @Getter @Setter
    private String skuName;
    @Getter @Setter
    private BigDecimal skuPrice;
    @Getter @Setter
    private Integer num;

    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        return this.skuPrice.multiply(new BigDecimal(""+this.num));
    }

}
