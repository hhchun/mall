package com.hhchun.mall.access.common.utils;


import com.hhchun.mall.access.common.constant.ResultCodeConstant;
import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> success(Integer code, String message, T data) {
        return new R<>(code, message, data);
    }

    public static <T> R<T> success(String message, T data) {
        return success(ResultCodeConstant.SUCCESS.getCode(), message, data);
    }

    public static <T> R<T> success(T data) {
        return success(ResultCodeConstant.SUCCESS.getMessage(), data);
    }

    public static <T> R<T> success() {
        return success(ResultCodeConstant.SUCCESS.getMessage(), null);
    }

    public static <T> R<T> error(Integer code, String message, T data) {
        return new R<>(code, message, data);
    }

    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> error(String message) {
        return error(ResultCodeConstant.ERROR.getCode(), message);
    }
}
