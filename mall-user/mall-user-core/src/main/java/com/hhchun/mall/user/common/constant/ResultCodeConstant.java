package com.hhchun.mall.user.common.constant;

public enum ResultCodeConstant {
    ACCESS_DENIED(403, "访问被拒绝"),
    AUTHENTICATION_ERROR(401, "身份认证错误");

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
