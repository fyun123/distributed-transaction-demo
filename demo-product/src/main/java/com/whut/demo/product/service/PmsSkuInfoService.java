package com.whut.demo.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.demo.common.utils.PageUtils;
import com.whut.demo.product.entity.PmsSkuInfoEntity;
import com.whut.demo.product.vo.CartItemVo;
import com.whut.demo.product.vo.CartVo;

import java.util.List;
import java.util.Map;

/**
 * 商品详情
 *
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:26:45
 */
public interface PmsSkuInfoService extends IService<PmsSkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);



}

