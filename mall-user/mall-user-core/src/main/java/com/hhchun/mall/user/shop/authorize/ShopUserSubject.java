package com.hhchun.mall.user.shop.authorize;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ShopUserSubject {
    public static final ShopUserSubject ANONYMOUS = new ShopUserSubject(Long.MIN_VALUE);

    private final Long shopUserId;

    private ShopUserSubject(Long shopUserId) {
        this.shopUserId = shopUserId;
    }

    public static ShopUserSubject newSubject(Long shopUserId) {
        if (shopUserId == null) {
            return ANONYMOUS;
        }
        return new ShopUserSubject(shopUserId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopUserSubject that = (ShopUserSubject) o;
        return Objects.equals(shopUserId, that.shopUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopUserId);
    }
}
