package com.whut.demo.order.feign;

import com.whut.demo.common.utils.R;
import com.whut.demo.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient("demo-ware")
public interface WareFeignService {
    @PostMapping("/ware/wmsskuware/lock/order")
    R orderLockStock(@RequestBody WareSkuLockVo vo);
}
