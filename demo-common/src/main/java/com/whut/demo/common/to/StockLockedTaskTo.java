package com.whut.demo.common.to;

import lombok.Data;

@Data
public class StockLockedTaskTo {
    /**
     * taskId
     */
    private Long id;
    /**
     * 细节
     */
    private StockLockedTaskDetailTo detailTo;
}
