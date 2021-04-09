package com.whut.demo.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单任务细节
 * 
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:27:20
 */
@Data
@TableName("wms_order_task_detail")
public class WmsOrderTaskDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
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
