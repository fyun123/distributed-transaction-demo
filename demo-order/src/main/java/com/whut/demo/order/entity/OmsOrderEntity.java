package com.whut.demo.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 订单
 * 
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:25:59
 */
@Data
@TableName("oms_order")
public class OmsOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 订单号
	 */
	private String bizOrderId;
	/**
	 * 订单状态【0-新建；1-已完成；2-已取消】
	 */
	private Integer orderStatus;
	/**
	 * 支付金额
	 */
	private BigDecimal payment;

	/**
	 * 订单项
	 */
	@TableField(exist = false)
	private List<OmsOrderItemEntity> orderItemEntities;

}
