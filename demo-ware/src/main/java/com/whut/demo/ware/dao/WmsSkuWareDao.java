package com.whut.demo.ware.dao;

import com.whut.demo.ware.entity.WmsSkuWareEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author 一大岐
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
@Mapper
public interface WmsSkuWareDao extends BaseMapper<WmsSkuWareEntity> {

    Long lockStock(@Param("skuId") Long skuId, @Param("num") Integer num);

    void unlockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);
}
