package com.hhchun.mall.user.customer.provider.cache;

import com.google.common.collect.Lists;
import com.hhchun.mall.user.customer.event.*;
import com.hhchun.mall.user.customer.service.CustomerMenuService;
import com.hhchun.mall.user.customer.service.CustomerRoleMenuService;
import com.hhchun.mall.user.customer.service.CustomerRolePermissionService;
import com.hhchun.mall.user.customer.service.CustomerUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class CustomerAccessPermissionCacheListener {

    @Autowired
    private CustomerAccessPermissionCache customerAccessPermissionCache;

    @Autowired
    private CustomerRolePermissionService customerRolePermissionService;
    @Autowired
    private CustomerUserRoleService customerUserRoleService;
    @Autowired
    private CustomerMenuService customerMenuService;
    @Autowired
    private CustomerRoleMenuService customerRoleMenuService;


    @EventListener(value = CustomerPermissionEvent.class)
    public void listenCustomerPermissionEventInvalidateAllPermission(CustomerPermissionEvent event) {
        customerAccessPermissionCache.invalidateAllPermission();
    }

    @EventListener(value = CustomerPermissionEvent.class)
    public void listenCustomerPermissionEventInvalidateOvertPermission(CustomerPermissionEvent event) {
        customerAccessPermissionCache.invalidateOvertPermission();
    }

    @EventListener(value = CustomerPermissionEvent.class)
    public void listenCustomerPermissionEventInvalidateOwnedPermission(CustomerPermissionEvent event) {
        Long permissionId = event.getPermissionId();
        Action action = event.getAction();
        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> roleIds = customerRolePermissionService.getCustomerRoleIdsByPermissionId(permissionId);
            List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> roleIds = customerRolePermissionService.getCustomerRemovedRoleIdsByPermissionId(permissionId);
            List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = CustomerRolePermissionEvent.class)
    public void listenCustomerRolePermissionEventInvalidateOwnedPermission(CustomerRolePermissionEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = CustomerRoleEvent.class)
    public void listenCustomerRoleEventInvalidateOwnedPermission(CustomerRoleEvent event) {
        Long roleId = event.getRoleId();
        Action action = event.getAction();
        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(Lists.newArrayList(roleId));
            userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> userIds = customerUserRoleService.getCustomerRemovedUserIdsByRoleIds(Lists.newArrayList(roleId));
            userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = CustomerUserRoleEvent.class)
    public void listenCustomerUserRoleEventInvalidateOwnedPermission(CustomerUserRoleEvent event) {
        Long userId = event.getUserId();
        customerAccessPermissionCache.invalidateOwnedPermission(userId);
    }

    @EventListener(value = CustomerMenuEvent.class)
    public void listenCustomerMenuEventInvalidateOwnedPermission(CustomerMenuEvent event) {
        Action action = event.getAction();
        Long menuId = event.getMenuId();
        String lowerRoute = event.getLowerRoute();

        if (action.equals(Action.MODIFY) || action.equals(Action.SAVE)) {
            List<Long> lowerMenuIds = customerMenuService.getCustomerLowerMenuIdsByRoute(lowerRoute);
            List<Long> menuIds = Lists.newArrayList();
            menuIds.add(menuId);
            menuIds.addAll(lowerMenuIds);
            List<Long> roleIds = customerRoleMenuService.getCustomerRoleIdsByMenuIds(menuIds);
            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
        } else if (action.equals(Action.REMOVE)) {
            List<Long> lowerMenuIds = event.getLowerMenuIds();
            List<Long> menuIds = Lists.newArrayList();
            menuIds.add(menuId);
            menuIds.addAll(lowerMenuIds);
            List<Long> roleIds = customerRoleMenuService.getCustomerRemovedRoleIdsByMenuIds(menuIds);
            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(roleIds);
            userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
        }
    }

    @EventListener(value = CustomerMenuPermissionEvent.class)
    public void listenCustomerMenuPermissionEventInvalidateOwnedPermission(CustomerMenuPermissionEvent event) {
        Long menuId = event.getMenuId();
        String lowerRoute = event.getLowerRoute();
        List<Long> lowerMenuIds = customerMenuService.getCustomerLowerMenuIdsByRoute(lowerRoute);
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(menuId);
        menuIds.addAll(lowerMenuIds);
        List<Long> roleIds = customerRoleMenuService.getCustomerRoleIdsByMenuIds(menuIds);
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(roleIds);
        userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
    }

    @EventListener(value = CustomerRoleMenuEvent.class)
    public void listenCustomerRoleMenuEventEventInvalidateOwnedPermission(CustomerRoleMenuEvent event) {
        Long roleId = event.getRoleId();
        List<Long> userIds = customerUserRoleService.getCustomerUserIdsByRoleIds(Lists.newArrayList(roleId));
        userIds.forEach(userId -> customerAccessPermissionCache.invalidateOwnedPermission(userId));
    }

}
