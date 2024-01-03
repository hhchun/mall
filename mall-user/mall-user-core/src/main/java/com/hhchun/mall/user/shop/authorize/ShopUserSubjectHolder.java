package com.hhchun.mall.user.shop.authorize;

import org.springframework.lang.Nullable;

public class ShopUserSubjectHolder {
    private static final ThreadLocal<ShopUserSubject> SUBJECT = new ThreadLocal<>();

    @Nullable
    public static ShopUserSubject getSubject() {
        return SUBJECT.get();
    }

    public static void setSubject(@Nullable ShopUserSubject subject) {
        SUBJECT.set(subject);
    }

    @Nullable
    public static Long getShopUserId() {
        ShopUserSubject subject = getSubject();
        if (subject == null) {
            return null;
        }
        return subject.getShopUserId();
    }

    public static void clearSubject() {
        SUBJECT.remove();
    }
}
