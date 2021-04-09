package com.whut.demo.product.service;

import com.whut.demo.product.vo.CartItemVo;
import com.whut.demo.product.vo.CartVo;

import java.util.List;

public interface CartService {
    CartVo addToRedisAndList();

    void countAdd(String skuId);

    void countSub(String skuId);

    void checkItem(Long skuId, Integer check);

    List<CartItemVo> getCurrentCartItems();

}
