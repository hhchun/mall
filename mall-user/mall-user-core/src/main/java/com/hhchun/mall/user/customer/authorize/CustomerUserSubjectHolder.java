package com.hhchun.mall.user.customer.authorize;

import org.springframework.lang.Nullable;

public class CustomerUserSubjectHolder {
    private static final ThreadLocal<CustomerUserSubject> SUBJECT = new ThreadLocal<>();

    @Nullable
    public static CustomerUserSubject getSubject() {
        return SUBJECT.get();
    }

    public static void setSubject(@Nullable CustomerUserSubject subject) {
        SUBJECT.set(subject);
    }

    @Nullable
    public static Long getCustomerUserId() {
        CustomerUserSubject subject = getSubject();
        if (subject == null) {
            return null;
        }
        return subject.getCustomerUserId();
    }

    public static void clearSubject() {
        SUBJECT.remove();
    }
}
