package com.whut.demo.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayRespVo {

    private String bizOrderId;

    private Integer code;
}
