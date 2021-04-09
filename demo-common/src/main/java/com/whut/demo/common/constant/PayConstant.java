package com.whut.demo.common.constant;

public class PayConstant {

    public enum PayRespConstant{
        SUCCESS(0,"支付成功"),FAILED(1,"支付失败"),
        SECOND_PAY(2,"重复支付"),ORDER_NON_EXIST(3,"订单不存在");
        private int code;
        private String msg;
        PayRespConstant(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
