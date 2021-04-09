package com.whut.demo.product.web;


import com.whut.demo.product.service.CartService;
import com.whut.demo.product.vo.CartItemVo;
import com.whut.demo.product.vo.CartVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class WebProductController {
    @Autowired
    CartService cartService;
    @GetMapping("/")
    public String index(Model model){
        CartVo cartVo = cartService.addToRedisAndList();
        model.addAttribute("cartVo",cartVo);
        return "index";
    }
    @GetMapping("/countAdd")
    public String countAdd(@Param("skuId") String skuId){
        cartService.countAdd(skuId);
        return "redirect:http://product.demo.com:6200";
    }

    @GetMapping("/countSub")
    public String countSub(@Param("skuId") String skuId){
        cartService.countSub(skuId);
        return "redirect:http://product.demo.com:6200";
    }

    @GetMapping("/checkItem")
    public String checkItem(@Param("skuId") Long skuId,
                            @Param("check")  Integer check){
        cartService.checkItem(skuId,check);
        return "redirect:http://product.demo.com:6200";
    }

    @ResponseBody
    @GetMapping("/getCurrentCartItems")
    public List<CartItemVo> getCurrentCart(){
        List<CartItemVo> cartItems = cartService.getCurrentCartItems();
        return cartItems;
    }
}
