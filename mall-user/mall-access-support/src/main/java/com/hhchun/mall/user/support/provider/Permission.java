package com.hhchun.mall.user.support.provider;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.Nullable;
import java.util.Objects;

public class Permission {
    /**
     * 序列化类型
     */
    public static final TypeReference<Permission> TYPE_REFERENCE = new TypeReference<Permission>() {
    };

    /**
     * 权限标识
     */
    private String symbol;
    /**
     * 额外信息
     */
    private Extra extra;

    public Permission() {
    }

    public Permission(String symbol) {
        this.symbol = symbol;
    }

    public Permission(String symbol, Extra extra) {
        this(symbol);
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

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public static class Extra {
        /**
         * 权限id
         */
        private Long permissionId;
        /**
         * 权限主体
         */
        private String subject;

        public Extra() {

        }

        public Extra(String subject) {
            this.subject = subject;
        }

        public Extra(String subject, Long permissionId) {
            this(subject);
            this.permissionId = permissionId;
        }

        @Nullable
        public Long getPermissionId() {
            return permissionId;
        }

        public String getSubject() {
            return subject;
        }

        public void setPermissionId(Long permissionId) {
            this.permissionId = permissionId;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}
