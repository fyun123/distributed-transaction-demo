package com.whut.demo.common.constant;

public class WareConstant {

    public enum WareTaskStatusConstant{
        LOCKED(0,"锁定"),UNLOCKED(1,"已解锁"),
        FINISH(2,"已扣减");
        private int code;
        private String msg;
        WareTaskStatusConstant(int code, String msg) {
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
