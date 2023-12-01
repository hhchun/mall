package com.hhchun.mall.access.platform.authorize;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PlatformUserSubject {
    public static final PlatformUserSubject ANONYMOUS = new PlatformUserSubject(Long.MIN_VALUE);

    private final Long platformUserId;

    private PlatformUserSubject(Long platformUserId) {
        this.platformUserId = platformUserId;
    }

    public static PlatformUserSubject newSubject(Long platformUserId) {
        if (platformUserId == null) {
            return ANONYMOUS;
        }
        return new PlatformUserSubject(platformUserId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformUserSubject that = (PlatformUserSubject) o;
        return Objects.equals(platformUserId, that.platformUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(platformUserId);
    }
}
