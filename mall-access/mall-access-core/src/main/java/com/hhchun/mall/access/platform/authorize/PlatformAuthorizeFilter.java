package com.hhchun.mall.access.platform.authorize;
import com.hhchun.mall.access.common.utils.TokenUtils;
import com.hhchun.mall.access.support.filter.AccessControlFilter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class PlatformAuthorizeFilter implements Filter {
    /**
     * 注册顺序
     */
    public static final int FILTER_REGISTRATION_ORDER = AccessControlFilter.FILTER_REGISTRATION_ORDER - 1;

    public static final String TOKEN_HEADER_NAME = "platformToken";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(TOKEN_HEADER_NAME);
        Long platformUserId = TokenUtils.getPlatformUserId(token);
        PlatformUserSubject subject = PlatformUserSubject.newSubject(platformUserId);
        PlatformUserSubjectHolder.setSubject(subject);
        chain.doFilter(request, response);
        PlatformUserSubjectHolder.clearSubject();
    }
}
