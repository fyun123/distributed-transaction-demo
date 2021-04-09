package com.whut.demo.common.to;

import lombok.Data;

@Data
public class StockLockedTaskDetailTo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 商品购买数量
     */
    private Integer skuNum;
    /**
     * 库存锁定状态【0-锁定；1-已解锁；2-已扣减】
     */
    private Integer lockStatus;
}
