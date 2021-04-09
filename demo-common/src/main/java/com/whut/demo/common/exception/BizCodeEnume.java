package com.whut.demo.common.exception;

public enum BizCodeEnume {

    NO_STOCK_EXCEPTION(21000,"商品库存不足");



    private int code;
    private String msg;
    BizCodeEnume(int code, String msg) {
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
