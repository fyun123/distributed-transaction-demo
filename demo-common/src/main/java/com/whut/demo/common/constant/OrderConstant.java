package com.whut.demo.common.constant;

public class OrderConstant {

    public enum OrderStatusConstant{
        CREATE(0,"新建"),PAYED(1,"已付款"),
        CANCEL(2,"已取消");
        private int code;
        private String msg;
        OrderStatusConstant(int code, String msg) {
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
