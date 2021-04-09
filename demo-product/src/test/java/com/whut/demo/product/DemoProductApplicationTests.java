package com.whut.demo.product;

import com.whut.demo.product.entity.PmsSkuInfoEntity;
import com.whut.demo.product.service.PmsSkuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoProductApplicationTests {
    @Autowired
    PmsSkuInfoService pmsSkuInfoService;

    @Test
    void contextLoads() {
        List<PmsSkuInfoEntity> list = pmsSkuInfoService.list();
        System.out.println(list);
    }

}
