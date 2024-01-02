package com.hhchun.mall.common.exception.advice;

import com.hhchun.mall.common.constant.ResultCodeConstant;
import com.hhchun.mall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestControllerAdvice
@Order
public class DefaultExceptionControllerAdvice {

    @ExceptionHandler(value = RuntimeException.class)
    public R<?> defaultRuntimeExceptionHandler(RuntimeException e, HttpServletRequest request) {
        log.error("request url：{}", request.getRequestURI());
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error(ResultCodeConstant.ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public R<?> defaultExceptionHandler(Exception e, HttpServletRequest request) {
        log.error("request url：{}", request.getRequestURI());
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error(ResultCodeConstant.ERROR.getCode(), e.getMessage());
    }

}
