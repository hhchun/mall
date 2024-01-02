package com.hhchun.mall.user.platform.authorize;

import org.springframework.lang.Nullable;

public class PlatformUserSubjectHolder {
    private static final ThreadLocal<PlatformUserSubject> SUBJECT = new ThreadLocal<>();

    @Nullable
    public static PlatformUserSubject getSubject() {
        return SUBJECT.get();
    }

    public static void setSubject(@Nullable PlatformUserSubject subject) {
        SUBJECT.set(subject);
    }

    @Nullable
    public static Long getPlatformUserId() {
        PlatformUserSubject subject = getSubject();
        if (subject == null) {
            return null;
        }
        return subject.getPlatformUserId();
    }

    public static void clearSubject() {
        SUBJECT.remove();
    }
}
