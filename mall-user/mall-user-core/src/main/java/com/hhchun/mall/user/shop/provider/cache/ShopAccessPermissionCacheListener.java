package com.hhchun.mall.user.shop.provider.cache;

import com.google.common.collect.Lists;
import com.hhchun.mall.user.shop.event.*;
import com.hhchun.mall.user.shop.service.ShopMenuService;
import com.hhchun.mall.user.shop.service.ShopRoleMenuService;
import com.hhchun.mall.user.shop.service.ShopRolePermissionService;
import com.hhchun.mall.user.shop.service.ShopUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ShopAccessPermissionCacheListener {

    @Autowired
    private ShopAccessPermissionCache shopAccessPermissionCache;

    @Autowired
    private ShopRolePermissionService shopRolePermissionService;
    @Autowired
    private ShopUserRoleService shopUserRoleService;
    @Autowired
    private ShopMenuService shopMenuService;
    @Autowired
    private ShopRoleMenuService shopRoleMenuService;


    @EventListener(value = ShopPermissionEvent.class)
    public void listenShopPermissionEventInvalidateAllPermission(ShopPermissionEvent event) {
        shopAccessPermissionCache.invalidateAllPermission();
    }

    @EventListener(value = ShopPermissionEvent.class)
    public void listenShopPermissionEventInvalidateOvertPermission(ShopPermissionEvent event) {
        shopAccessPermissionCache.invalidateOvertPermission();
    }

    @EventListener(value = ShopPermissionEvent.class)
    public void listenShopPermissionEventInvalidateOwnedPermission(ShopPermissionEvent event) {
        Long permissionId = event.getPermissionId();
        Action action = event.getAction();
        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> roleIds = shopRolePermissionService.getShopRoleIdsByPermissionId(permissionId);
            List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> roleIds = shopRolePermissionService.getShopRemovedRoleIdsByPermissionId(permissionId);
            List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = ShopRolePermissionEvent.class)
    public void listenShopRolePermissionEventInvalidateOwnedPermission(ShopRolePermissionEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = ShopRoleEvent.class)
    public void listenShopRoleEventInvalidateOwnedPermission(ShopRoleEvent event) {
        Long roleId = event.getRoleId();
        Action action = event.getAction();
        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(Lists.newArrayList(roleId));
            userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> userIds = shopUserRoleService.getShopRemovedUserIdsByRoleIds(Lists.newArrayList(roleId));
            userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = ShopUserRoleEvent.class)
    public void listenShopUserRoleEventInvalidateOwnedPermission(ShopUserRoleEvent event) {
        Long userId = event.getUserId();
        shopAccessPermissionCache.invalidateOwnedPermission(userId);
    }

    @EventListener(value = ShopMenuEvent.class)
    public void listenShopMenuEventInvalidateOwnedPermission(ShopMenuEvent event) {
        Action action = event.getAction();
        Long menuId = event.getMenuId();
        String lowerRoute = event.getLowerRoute();

        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> lowerMenuIds = shopMenuService.getShopLowerMenuIdsByRoute(lowerRoute);
            List<Long> menuIds = Lists.newArrayList();
            menuIds.add(menuId);
            menuIds.addAll(lowerMenuIds);
            List<Long> roleIds = shopRoleMenuService.getShopRoleIdsByMenuIds(menuIds);
            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> lowerMenuIds = event.getLowerMenuIds();
            List<Long> menuIds = Lists.newArrayList();
            menuIds.add(menuId);
            menuIds.addAll(lowerMenuIds);
            List<Long> roleIds = shopRoleMenuService.getShopRemovedRoleIdsByMenuIds(menuIds);
            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = ShopMenuPermissionEvent.class)
    public void listenShopMenuPermissionEventInvalidateOwnedPermission(ShopMenuPermissionEvent event) {
        Long menuId = event.getMenuId();
        String lowerRoute = event.getLowerRoute();
        List<Long> lowerMenuIds = shopMenuService.getShopLowerMenuIdsByRoute(lowerRoute);
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(menuId);
        menuIds.addAll(lowerMenuIds);
        List<Long> roleIds = shopRoleMenuService.getShopRoleIdsByMenuIds(menuIds);
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(roleIds);
        userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = ShopRoleMenuEvent.class)
    public void listenShopRoleMenuEventEventInvalidateOwnedPermission(ShopRoleMenuEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = shopUserRoleService.getShopUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> shopAccessPermissionCache.invalidateOwnedPermission(userId));
    }

}
