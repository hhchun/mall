package com.hhchun.mall.user.fingerprint;

import org.springframework.lang.Nullable;

public class FingerprintHolder {
    private static final ThreadLocal<Fingerprint> FINGERPRINT = new ThreadLocal<>();

    @Nullable
    public static Fingerprint getFingerprint() {
        return FINGERPRINT.get();
    }

    public static void setFingerprint(@Nullable Fingerprint fingerprint) {
        FINGERPRINT.set(fingerprint);
    }

    public static void clearFingerprint() {
        FINGERPRINT.remove();
    }
}
