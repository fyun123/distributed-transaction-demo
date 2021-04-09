package com.whut.demo.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单项
 * 
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:25:59
 */
@Data
@TableName("oms_order_item")
public class OmsOrderItemEntity implements Serializable {
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
	 * 商品id
	 */
	private Long skuId;
	/**
	 * 商品名称
	 */
	private String skuName;
	/**
	 * 商品单价
	 */
	private BigDecimal skuPrice;
	/**
	 * 购买数量
	 */
	private Integer skuNum;
	/**
	 * 商品价格
	 */
	private BigDecimal amount;

}
