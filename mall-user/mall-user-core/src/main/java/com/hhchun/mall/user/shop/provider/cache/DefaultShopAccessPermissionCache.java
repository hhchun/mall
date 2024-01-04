package com.hhchun.mall.user.shop.provider.cache;


import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.hhchun.mall.user.shop.constant.ShopMenuConstant;
import com.hhchun.mall.user.shop.constant.ShopPermissionConstant;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuEntity;
import com.hhchun.mall.user.shop.entity.domain.ShopPermissionEntity;
import com.hhchun.mall.user.shop.service.*;
import com.hhchun.mall.access.support.provider.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 没有使用缓存
 */
//@Component
public class DefaultShopAccessPermissionCache implements ShopAccessPermissionCache {

    @Autowired
    private ShopPermissionService shopPermissionService;
    @Autowired
    private ShopUserRoleService shopUserRoleService;
    @Autowired
    private ShopRolePermissionService shopRolePermissionService;
    @Autowired
    private ShopRoleMenuService shopRoleMenuService;
    @Autowired
    private ShopMenuPermissionService shopMenuPermissionService;
    @Autowired
    private ShopMenuService shopMenuService;

    @Override
    public List<Permission> getAllPermission() {
        LambdaQueryWrapper<ShopPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ShopPermissionEntity::getSymbol, ShopPermissionEntity::getSubject);
        return shopPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOwnedPermission(Long shopUserId) {
        List<Long> roleIds = shopUserRoleService.getShopRoleIdsByUserId(shopUserId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        // 1.角色关联权限
        List<Long> permissionIds = shopRolePermissionService.getShopPermissionIdsByRoleIds(roleIds);

        // 2.角色关联菜单,菜单关联权限
        List<Long> menuIds = shopRoleMenuService.getShopMenuIdsByRoleIds(roleIds);
        List<Long> upperMenuIds = shopMenuService.getShopUpperMenuIdsByMenuIds(menuIds);
        menuIds.addAll(upperMenuIds);
        List<Long> menuToPermissionIds = shopMenuPermissionService.getShopPermissionIdsByMenuIds(menuIds);
        permissionIds.addAll(menuToPermissionIds);

        if (CollectionUtils.isEmpty(permissionIds)) {
            return Lists.newArrayList();
        }

        LambdaQueryWrapper<ShopPermissionEntity> wrapper = new LambdaQueryWrapper<ShopPermissionEntity>()
                .select(ShopPermissionEntity::getSymbol)
                .in(ShopPermissionEntity::getId, permissionIds);
        return shopPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOvertPermission() {
        List<ShopMenuEntity> menus = shopMenuService.list(new LambdaQueryWrapper<ShopMenuEntity>()
                .select(ShopMenuEntity::getId, ShopMenuEntity::getRoute)
                .eq(ShopMenuEntity::getOvert, ShopMenuConstant.MENU_OVERT_YES));
        List<Long> menuIds = menus.stream().map(ShopMenuEntity::getId).collect(Collectors.toList());
        List<Long> upperMenuIds = menus.stream()
                .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                .map(Long::valueOf)
                .collect(Collectors.toList());
        menuIds.addAll(upperMenuIds);
        List<Long> permissionIds = shopMenuPermissionService.getShopPermissionIdsByMenuIds(menuIds);
        LambdaQueryWrapper<ShopPermissionEntity> wrapper = new LambdaQueryWrapper<ShopPermissionEntity>()
                .select(ShopPermissionEntity::getSymbol)
                .eq(ShopPermissionEntity::getOvert, ShopPermissionConstant.PERMISSION_OVERT_YES)
                .or().in(!CollectionUtils.isEmpty(permissionIds), ShopPermissionEntity::getId, permissionIds);
        return shopPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void invalidateAllPermission() {
        // null
    }

    @Override
    public void invalidateOwnedPermission(Long shopUserId) {
        // null
    }

    @Override
    public void invalidateOvertPermission() {
        // null
    }
}
