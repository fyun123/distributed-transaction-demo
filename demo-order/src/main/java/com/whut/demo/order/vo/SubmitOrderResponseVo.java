package com.whut.demo.order.vo;

import com.whut.demo.order.entity.OmsOrderEntity;
import lombok.Data;

import java.util.List;

@Data
public class SubmitOrderResponseVo {

    private OmsOrderEntity order;

    private Integer code; // 错误状态码，0-成功，1-失败

}
