package com.hhchun.mall.access.support.filter;

import com.google.common.base.Preconditions;
import com.hhchun.mall.access.support.decision.AccessDecision;
import com.hhchun.mall.access.support.denied.AccessDenied;

import javax.servlet.*;
import java.io.IOException;

/**
 * 权限控制过滤器
 */
public class AccessSupportFilter implements Filter {
    /**
     * 注册顺序
     */
    public static final int FILTER_REGISTRATION_ORDER = Integer.MAX_VALUE;
    /**
     * 访问决策器
     */
    private final AccessDecision accessDecision;
    /**
     * 访问拒绝器
     */
    private final AccessDenied accessDenied;

    public AccessSupportFilter(final AccessDecision accessDecision, final AccessDenied accessDenied) {
        Preconditions.checkArgument(accessDecision != null, "accessDecision == null!");
        Preconditions.checkArgument(accessDenied != null, "accessDenied == null!");
        this.accessDecision = accessDecision;
        this.accessDenied = accessDenied;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (accessDecision.decide()) {
            chain.doFilter(request, response);
        } else {
            accessDenied.denied();
        }
    }
}
