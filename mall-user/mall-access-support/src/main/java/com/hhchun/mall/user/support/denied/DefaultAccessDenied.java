package com.hhchun.mall.user.support.denied;

import com.hhchun.mall.user.support.exception.AccessDeniedException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultAccessDenied implements AccessDenied {
    private final HandlerExceptionResolver resolver;

    public DefaultAccessDenied(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void denied() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();
        assert response != null;
        resolver.resolveException(request, response, null, new AccessDeniedException());
    }
}
