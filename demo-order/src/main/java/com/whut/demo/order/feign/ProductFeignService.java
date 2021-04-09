package com.whut.demo.order.feign;

import com.whut.demo.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient("demo-product")
public interface ProductFeignService {

    @GetMapping("/getCurrentCartItems")
    List<OrderItemVo> getCurrentCart();
}
