package com.hhchun.mall.access.support.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({AccessControlWebConfiguration.class})
@Documented
public @interface EnableAccessControl {

}
