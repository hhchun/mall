package com.hhchun.mall.access.support.provider;

import java.util.Objects;

public class Permission {
    /**
     * 权限标识
     */
    private final String symbol;

    /**
     * 权限主体
     */
    private final String subject;

    public Permission(String symbol) {
        this.symbol = symbol;
        this.subject = null;
    }

    public Permission(String symbol, String subject) {
        this.symbol = symbol;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }
}
