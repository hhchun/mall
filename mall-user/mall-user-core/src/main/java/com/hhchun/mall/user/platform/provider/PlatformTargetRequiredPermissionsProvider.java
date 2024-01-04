package com.hhchun.mall.user.platform.provider;

import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.user.fingerprint.Fingerprint;
import com.hhchun.mall.user.fingerprint.FingerprintHolder;
import com.hhchun.mall.user.platform.provider.cache.PlatformAccessPermissionCache;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.access.support.provider.TargetRequiredPermissionsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class PlatformTargetRequiredPermissionsProvider implements TargetRequiredPermissionsProvider {

    private final PlatformAccessPermissionCache cache;

    public PlatformTargetRequiredPermissionsProvider(PlatformAccessPermissionCache cache) {
        this.cache = cache;
    }

    @Override
    public boolean support() {
        Fingerprint fingerprint = FingerprintHolder.getFingerprint();
        if (fingerprint != null) {
            return Fingerprint.CLIENT_TYPE_PLATFORM.equals(fingerprint.getClientType());
        }
        return true;
    }

    @Override
    public List<Permission> provide() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String target = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        List<Permission> all = cache.getAllPermission();
        return all.stream().filter(p -> {
            Permission.Extra extra = p.getExtra();
            Preconditions.checkArgument(extra != null, "extra == null!");
            String subject = extra.getSubject();
            return StringUtils.hasLength(subject) && matcher.match(subject, target);
        }).collect(Collectors.toList());
    }
}
