package com.hhchun.mall.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Object> {

    private final Set<Object> set = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        String[] values = constraintAnnotation.values();
        Collections.addAll(set, values);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null){
            // 不处理空值
            return true;
        }
        if (!(value instanceof String)){
            return set.contains(value.toString());
        }else {
            return set.contains(value);
        }
    }
}
