package com.hhchun.mall.user.support.provider;

import javax.annotation.Nullable;
import java.util.Objects;

public class Permission {
    /**
     * 权限标识
     */
    private final String symbol;
    /**
     * 额外信息
     */
    private final Extra extra;

    public Permission(String symbol) {
        this.symbol = symbol;
        this.extra = null;
    }

    public Permission(String symbol, Extra extra) {
        this.symbol = symbol;
        this.extra = extra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    @Nullable
    public Extra getExtra() {
        return extra;
    }

    public static class Extra {
        /**
         * 权限id
         */
        private final Long permissionId;
        /**
         * 权限主体
         */
        private final String subject;

        public Extra(String subject) {
            this.subject = subject;
            this.permissionId = null;
        }

        public Extra(Long permissionId, String subject) {
            this.permissionId = permissionId;
            this.subject = subject;
        }

        @Nullable
        public Long getPermissionId() {
            return permissionId;
        }

        public String getSubject() {
            return subject;
        }
    }
}
