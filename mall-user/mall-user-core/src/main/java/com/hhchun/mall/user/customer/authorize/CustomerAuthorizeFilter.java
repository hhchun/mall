package com.hhchun.mall.user.customer.authorize;

import com.hhchun.mall.access.support.filter.AccessSupportFilter;
import com.hhchun.mall.common.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class CustomerAuthorizeFilter implements Filter {
    /**
     * 注册顺序
     */
    public static final int FILTER_REGISTRATION_ORDER = AccessSupportFilter.FILTER_REGISTRATION_ORDER - 1;

    public static final String TOKEN_HEADER_NAME = "customerToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(TOKEN_HEADER_NAME);
        Long customerUserId = TokenUtils.getUserId(token);
        CustomerUserSubject subject = CustomerUserSubject.newSubject(customerUserId);
        CustomerUserSubjectHolder.setSubject(subject);
        chain.doFilter(request, response);
        CustomerUserSubjectHolder.clearSubject();
    }
}
