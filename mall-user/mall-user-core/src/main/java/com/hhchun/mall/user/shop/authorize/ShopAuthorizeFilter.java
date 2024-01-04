package com.hhchun.mall.user.shop.authorize;
import com.hhchun.mall.common.utils.TokenUtils;
import com.hhchun.mall.access.support.filter.AccessSupportFilter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class ShopAuthorizeFilter implements Filter {
    /**
     * 注册顺序
     */
    public static final int FILTER_REGISTRATION_ORDER = AccessSupportFilter.FILTER_REGISTRATION_ORDER - 1;

    public static final String TOKEN_HEADER_NAME = "shopToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(TOKEN_HEADER_NAME);
        Long shopUserId = TokenUtils.getUserId(token);
        ShopUserSubject subject = ShopUserSubject.newSubject(shopUserId);
        ShopUserSubjectHolder.setSubject(subject);
        chain.doFilter(request, response);
        ShopUserSubjectHolder.clearSubject();
    }
}
