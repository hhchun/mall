package com.hhchun.mall.user.customer.provider.cache;


import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.user.customer.constant.CustomerMenuConstant;
import com.hhchun.mall.user.customer.constant.CustomerPermissionConstant;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuEntity;
import com.hhchun.mall.user.customer.entity.domain.CustomerPermissionEntity;
import com.hhchun.mall.user.customer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 没有使用缓存
 */
//@Component
public class DefaultCustomerAccessPermissionCache implements CustomerAccessPermissionCache {

    @Autowired
    private CustomerPermissionService customerPermissionService;
    @Autowired
    private CustomerUserRoleService customerUserRoleService;
    @Autowired
    private CustomerRolePermissionService customerRolePermissionService;
    @Autowired
    private CustomerRoleMenuService customerRoleMenuService;
    @Autowired
    private CustomerMenuPermissionService customerMenuPermissionService;
    @Autowired
    private CustomerMenuService customerMenuService;

    @Override
    public List<Permission> getAllPermission() {
        LambdaQueryWrapper<CustomerPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CustomerPermissionEntity::getSymbol, CustomerPermissionEntity::getSubject);
        return customerPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOwnedPermission(Long customerUserId) {
        List<Long> roleIds = customerUserRoleService.getCustomerRoleIdsByUserId(customerUserId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        // 1.角色关联权限
        List<Long> permissionIds = customerRolePermissionService.getCustomerPermissionIdsByRoleIds(roleIds);

        // 2.角色关联菜单,菜单关联权限
        List<Long> menuIds = customerRoleMenuService.getCustomerMenuIdsByRoleIds(roleIds);
        List<Long> upperMenuIds = customerMenuService.getCustomerUpperMenuIdsByMenuIds(menuIds);
        menuIds.addAll(upperMenuIds);
        List<Long> menuToPermissionIds = customerMenuPermissionService.getCustomerPermissionIdsByMenuIds(menuIds);
        permissionIds.addAll(menuToPermissionIds);

        if (CollectionUtils.isEmpty(permissionIds)) {
            return Lists.newArrayList();
        }

        LambdaQueryWrapper<CustomerPermissionEntity> wrapper = new LambdaQueryWrapper<CustomerPermissionEntity>()
                .select(CustomerPermissionEntity::getSymbol)
                .in(CustomerPermissionEntity::getId, permissionIds);
        return customerPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOvertPermission() {
        List<CustomerMenuEntity> menus = customerMenuService.list(new LambdaQueryWrapper<CustomerMenuEntity>()
                .select(CustomerMenuEntity::getId, CustomerMenuEntity::getRoute)
                .eq(CustomerMenuEntity::getOvert, CustomerMenuConstant.MENU_OVERT_YES));
        List<Long> menuIds = menus.stream().map(CustomerMenuEntity::getId).collect(Collectors.toList());
        List<Long> upperMenuIds = menus.stream()
                .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                .map(Long::valueOf)
                .collect(Collectors.toList());
        menuIds.addAll(upperMenuIds);
        List<Long> permissionIds = customerMenuPermissionService.getCustomerPermissionIdsByMenuIds(menuIds);
        LambdaQueryWrapper<CustomerPermissionEntity> wrapper = new LambdaQueryWrapper<CustomerPermissionEntity>()
                .select(CustomerPermissionEntity::getSymbol)
                .eq(CustomerPermissionEntity::getOvert, CustomerPermissionConstant.PERMISSION_OVERT_YES)
                .or().in(!CollectionUtils.isEmpty(permissionIds), CustomerPermissionEntity::getId, permissionIds);
        return customerPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void invalidateAllPermission() {
        // null
    }

    @Override
    public void invalidateOwnedPermission(Long customerUserId) {
        // null
    }

    @Override
    public void invalidateOvertPermission() {
        // null
    }
}
