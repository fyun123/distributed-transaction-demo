package com.whut.demo.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品详情
 * 
 * @author ä¸å¤§å²
 * @email 1184384017@qq.com
 * @date 2021-04-07 15:26:45
 */
@Data
@TableName("pms_sku_info")
public class PmsSkuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableId
	private Long skuId;
	/**
	 * 商品名称
	 */
	private String skuName;
	/**
	 * 商品价格
	 */
	private BigDecimal skuPrice;

}
