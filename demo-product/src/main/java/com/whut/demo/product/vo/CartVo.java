package com.whut.demo.product.vo;



import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    @Getter
    @Setter
    private List<CartItemVo> items;

    private Integer count;

    private BigDecimal totalAmount;

    public Integer getCount() {
        int count = 0;
        if (items != null && items.size() > 0) {
            for (CartItemVo item : items) {
                count += item.getNum();
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if (items != null && items.size() > 0) {
            for (CartItemVo item : items) {
                if (item.getCheck()){
                    BigDecimal totalPrice = item.getTotalPrice();
                    amount = amount.add(totalPrice);
                }
            }
        }
        return amount;
    }
}
