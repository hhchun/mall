package com.hhchun.mall.access.platform.provider;

import com.hhchun.mall.access.platform.authorize.PlatformUserSubjectHolder;
import com.hhchun.mall.access.platform.provider.cache.PlatformAccessPermissionCache;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.access.support.provider.SubjectOwnedPermissionsProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PlatformSubjectOwnedPermissionsProvider implements SubjectOwnedPermissionsProvider {

    private final PlatformAccessPermissionCache cache;

    public PlatformSubjectOwnedPermissionsProvider(PlatformAccessPermissionCache cache) {
        this.cache = cache;
    }

    @Override
    public List<Permission> provide() {
        Long platformUserId = PlatformUserSubjectHolder.getPlatformUserId();
        List<Permission> overt = cache.getOvertPermission();
        List<Permission> owned = cache.getOwnedPermission(platformUserId);
        return Stream.concat(overt.stream(), owned.stream()).collect(Collectors.toList());
    }
}
