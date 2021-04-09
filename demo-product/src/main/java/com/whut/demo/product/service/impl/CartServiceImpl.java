package com.whut.demo.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.common.utils.Query;
import com.whut.demo.product.dao.PmsSkuInfoDao;
import com.whut.demo.product.entity.PmsSkuInfoEntity;
import com.whut.demo.product.service.CartService;
import com.whut.demo.product.service.PmsSkuInfoService;
import com.whut.demo.product.vo.CartItemVo;
import com.whut.demo.product.vo.CartVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl  implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    PmsSkuInfoService skuInfoService;

    private final String CART_KEY = "demo:cart";

    @Override
    public CartVo addToRedisAndList() {
        CartVo cartVo = new CartVo();
        List<CartItemVo> cartItemVos = new ArrayList<>();
        // skuId : cartItem
        BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(CART_KEY);
        List<PmsSkuInfoEntity> skuInfos = skuInfoService.list();
        for (PmsSkuInfoEntity skuInfo : skuInfos) {
            String res = (String) cartOps.get(skuInfo.getSkuId().toString());
            if (StringUtils.isEmpty(res)) {
                // 购物车中无此商品
                CartItemVo cartItem = new CartItemVo();
                // 添加新商品到购物车
                cartItem.setCheck(true);
                cartItem.setSkuId(skuInfo.getSkuId());
                cartItem.setNum(1);
                cartItem.setSkuName(skuInfo.getSkuName());
                cartItem.setSkuPrice(skuInfo.getSkuPrice());
                cartOps.put(skuInfo.getSkuId().toString(), JSON.toJSONString(cartItem));
                cartItemVos.add(cartItem);
            } else {
                // 购物车有此商品
                CartItemVo cartItemOld = JSON.parseObject(res, CartItemVo.class);
                cartItemVos.add(cartItemOld);
            }
            cartVo.setItems(cartItemVos);
        }
        return cartVo;
    }

    @Override
    public void countAdd(String skuId) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(CART_KEY);
        String res = (String) hashOps.get(skuId);
        if (!StringUtils.isEmpty(res)) {
            // 购物车有此商品
            CartItemVo cartItemOld = JSON.parseObject(res, CartItemVo.class);
            cartItemOld.setNum(cartItemOld.getNum() + 1);
            hashOps.put(skuId, JSON.toJSONString(cartItemOld));
        }
    }

    @Override
    public void countSub(String skuId) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(CART_KEY);
        String res = (String) hashOps.get(skuId);
        if (!StringUtils.isEmpty(res)) {
            // 购物车有此商品
            CartItemVo cartItemOld = JSON.parseObject(res, CartItemVo.class);
            if (cartItemOld.getNum() > 1){
                cartItemOld.setNum(cartItemOld.getNum() - 1);
                hashOps.put(skuId, JSON.toJSONString(cartItemOld));
            }
        }
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = redisTemplate.boundHashOps(CART_KEY);
        String s = (String) cartOps.get(skuId.toString());
        CartItemVo cartItem = JSON.parseObject(s, CartItemVo.class);
        if (cartItem != null){
            cartItem.setCheck(check == 1);
        }
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public List<CartItemVo> getCurrentCartItems() {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(CART_KEY);
        List<String> cartItems = hashOps.values();
        if (cartItems != null && cartItems.size() > 0){
            List<CartItemVo> collect = cartItems.stream().map(item -> JSON.parseObject(item, CartItemVo.class)).filter(CartItemVo::getCheck).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

}