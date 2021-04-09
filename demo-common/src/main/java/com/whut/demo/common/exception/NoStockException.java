package com.whut.demo.common.exception;

import java.util.List;

public class NoStockException extends RuntimeException {


    private List<Long> skuIds;

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }

    public NoStockException(){
        super("没有足够的库存了");
    }


    public NoStockException(List<Long> skuIds){
        super("商品id:"+skuIds+",没有足够的库存了");
        this.skuIds = skuIds;
    }


}
