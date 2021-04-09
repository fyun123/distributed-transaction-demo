package com.whut.demo.ware.feign;

import com.whut.demo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("demo-order")
public interface OrderFeignService {

    @GetMapping("/order/omsorder/getOrderStatus/{bizOrderId}")
    R getOrderStatus(@PathVariable("bizOrderId") String bizOrderId);
}
