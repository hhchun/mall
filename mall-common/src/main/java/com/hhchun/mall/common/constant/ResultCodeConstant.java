package com.hhchun.mall.common.constant;

public enum ResultCodeConstant {
    SUCCESS(0, "成功"),
    ERROR(500, "系统错误"),
    ARGUMENT_ERROR(400, "参数错误"),
    ILLEGAL_CONDITION(410, "非法条件");

    ResultCodeConstant(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private Integer code;
    private String message;

}
