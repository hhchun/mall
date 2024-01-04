package com.hhchun.mall.user.customer.authorize;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CustomerUserSubject {
    public static final CustomerUserSubject ANONYMOUS = new CustomerUserSubject(Long.MIN_VALUE);

    private final Long customerUserId;

    private CustomerUserSubject(Long customerUserId) {
        this.customerUserId = customerUserId;
    }

    public static CustomerUserSubject newSubject(Long customerUserId) {
        if (customerUserId == null) {
            return ANONYMOUS;
        }
        return new CustomerUserSubject(customerUserId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerUserSubject that = (CustomerUserSubject) o;
        return Objects.equals(customerUserId, that.customerUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerUserId);
    }
}
