package com.hhchun.mall.access.platform.listener;

import com.google.common.collect.Lists;
import com.hhchun.mall.access.platform.event.PlatformPermissionEvent;
import com.hhchun.mall.access.platform.event.PlatformRoleEvent;
import com.hhchun.mall.access.platform.event.PlatformRolePermissionEvent;
import com.hhchun.mall.access.platform.event.PlatformUserRoleEvent;
import com.hhchun.mall.access.platform.provider.cache.PlatformAccessPermissionCache;
import com.hhchun.mall.access.platform.service.PlatformRolePermissionService;
import com.hhchun.mall.access.platform.service.PlatformUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class PlatformAccessPermissionCacheListener {

    @Autowired
    private PlatformAccessPermissionCache platformAccessPermissionCache;

    @Autowired
    private PlatformRolePermissionService platformRolePermissionService;
    @Autowired
    private PlatformUserRoleService platformUserRoleService;


    @EventListener(value = PlatformPermissionEvent.class)
    public void listenPlatformPermissionEventInvalidateAllPermission(PlatformPermissionEvent event) {
        platformAccessPermissionCache.invalidateAllPermission();
    }

    @EventListener(value = PlatformPermissionEvent.class)
    public void listenPlatformPermissionEventInvalidateOvertPermission(PlatformPermissionEvent event) {
        platformAccessPermissionCache.invalidateOvertPermission();
    }

    @EventListener(value = PlatformPermissionEvent.class)
    public void listenPlatformPermissionEventInvalidateOwnedPermission(PlatformPermissionEvent event) {
        Long permissionId = event.getPermissionId();
        List<Long> roleIds = platformRolePermissionService.getPlatformRoleIdsByPermissionId(permissionId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(roleIds);
        userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = PlatformRolePermissionEvent.class)
    public void listenPlatformRolePermissionEventInvalidateOwnedPermission(PlatformRolePermissionEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = PlatformRoleEvent.class)
    public void listenPlatformRoleEventInvalidateOwnedPermission(PlatformRoleEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = PlatformUserRoleEvent.class)
    public void listenPlatformUserRoleEventInvalidateOwnedPermission(PlatformUserRoleEvent event) {
        Long userId = event.getUserId();
        platformAccessPermissionCache.invalidateOwnedPermission(userId);
    }
}
