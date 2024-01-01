package com.hhchun.mall.access.platform.provider.cache;

import com.google.common.collect.Lists;
import com.hhchun.mall.access.platform.event.*;
import com.hhchun.mall.access.platform.service.PlatformMenuService;
import com.hhchun.mall.access.platform.service.PlatformRoleMenuService;
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
    @Autowired
    private PlatformMenuService platformMenuService;
    @Autowired
    private PlatformRoleMenuService platformRoleMenuService;


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
        Action action = event.getAction();
        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> roleIds = platformRolePermissionService.getPlatformRoleIdsByPermissionId(permissionId);
            List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> roleIds = platformRolePermissionService.getPlatformRemovedRoleIdsByPermissionId(permissionId);
            List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
        }
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
        Action action = event.getAction();
        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(Lists.newArrayList(roleId));
            userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> userIds = platformUserRoleService.getPlatformRemovedUserIdsByRoleIds(Lists.newArrayList(roleId));
            userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = PlatformUserRoleEvent.class)
    public void listenPlatformUserRoleEventInvalidateOwnedPermission(PlatformUserRoleEvent event) {
        Long userId = event.getUserId();
        platformAccessPermissionCache.invalidateOwnedPermission(userId);
    }

    @EventListener(value = PlatformMenuEvent.class)
    public void listenPlatformMenuEventInvalidateOwnedPermission(PlatformMenuEvent event) {
        Action action = event.getAction();
        Long menuId = event.getMenuId();
        String lowerRoute = event.getLowerRoute();

        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> lowerMenuIds = platformMenuService.getPlatformLowerMenuIdsByRoute(lowerRoute);
            List<Long> menuIds = Lists.newArrayList();
            menuIds.add(menuId);
            menuIds.addAll(lowerMenuIds);
            List<Long> roleIds = platformRoleMenuService.getPlatformRoleIdsByMenuIds(menuIds);
            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> lowerMenuIds = event.getLowerMenuIds();
            List<Long> menuIds = Lists.newArrayList();
            menuIds.add(menuId);
            menuIds.addAll(lowerMenuIds);
            List<Long> roleIds = platformRoleMenuService.getPlatformRemovedRoleIdsByMenuIds(menuIds);
            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = PlatformMenuPermissionEvent.class)
    public void listenPlatformMenuPermissionEventInvalidateOwnedPermission(PlatformMenuPermissionEvent event) {
        Long menuId = event.getMenuId();
        String lowerRoute = event.getLowerRoute();
        List<Long> lowerMenuIds = platformMenuService.getPlatformLowerMenuIdsByRoute(lowerRoute);
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(menuId);
        menuIds.addAll(lowerMenuIds);
        List<Long> roleIds = platformRoleMenuService.getPlatformRoleIdsByMenuIds(menuIds);
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(roleIds);
        userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = PlatformRoleMenuEvent.class)
    public void listenPlatformRoleMenuEventEventInvalidateOwnedPermission(PlatformRoleMenuEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = platformUserRoleService.getPlatformUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> platformAccessPermissionCache.invalidateOwnedPermission(userId));
    }

}
