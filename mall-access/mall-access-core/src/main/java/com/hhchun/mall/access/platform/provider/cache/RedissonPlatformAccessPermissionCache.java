package com.hhchun.mall.access.platform.provider.cache;

import com.hhchun.mall.access.support.provider.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedissonPlatformAccessPermissionCache extends DefaultPlatformAccessPermissionCache {
    @Override
    public List<Permission> getAllPermission() {
        return super.getAllPermission();
    }

    @Override
    public List<Permission> getOvertPermissions() {
        return super.getOvertPermissions();
    }

    @Override
    public List<Permission> getOwnedPermissions(Long platformUserId) {
        return super.getOwnedPermissions(platformUserId);
    }
}
