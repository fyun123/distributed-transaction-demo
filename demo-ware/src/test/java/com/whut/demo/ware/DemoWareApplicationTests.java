package com.whut.demo.ware;

import com.whut.demo.ware.service.WmsSkuWareService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoWareApplicationTests {

    @Autowired
    WmsSkuWareService skuWareService;

    @Test
    void contextLoads() {
        System.out.println(skuWareService.list());
    }

}
