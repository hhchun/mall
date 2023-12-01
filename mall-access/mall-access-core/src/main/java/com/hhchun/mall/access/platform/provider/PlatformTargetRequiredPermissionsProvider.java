package com.hhchun.mall.access.platform.provider;

import com.google.common.collect.Lists;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
public class PlatformTargetRequiredPermissionsProvider implements TargetRequiredPermissionsProvider {

    @Override
    public List<Permission> provide() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        return Lists.newArrayList(new Permission("develop"));
    }
}
