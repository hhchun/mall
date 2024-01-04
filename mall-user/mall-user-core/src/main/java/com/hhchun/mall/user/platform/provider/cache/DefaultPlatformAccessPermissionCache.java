package com.hhchun.mall.user.platform.provider.cache;


import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.hhchun.mall.user.platform.constant.PlatformMenuConstant;
import com.hhchun.mall.user.platform.constant.PlatformPermissionConstant;
import com.hhchun.mall.user.platform.entity.domain.*;
import com.hhchun.mall.user.platform.service.*;
import com.hhchun.mall.access.support.provider.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 没有使用缓存
 */
//@Component
public class DefaultPlatformAccessPermissionCache implements PlatformAccessPermissionCache {

    @Autowired
    private PlatformPermissionService platformPermissionService;
    @Autowired
    private PlatformUserRoleService platformUserRoleService;
    @Autowired
    private PlatformRolePermissionService platformRolePermissionService;
    @Autowired
    private PlatformRoleMenuService platformRoleMenuService;
    @Autowired
    private PlatformMenuPermissionService platformMenuPermissionService;
    @Autowired
    private PlatformMenuService platformMenuService;

    @Override
    public List<Permission> getAllPermission() {
        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformPermissionEntity::getSymbol, PlatformPermissionEntity::getSubject);
        return platformPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOwnedPermission(Long platformUserId) {
        List<Long> roleIds = platformUserRoleService.getPlatformRoleIdsByUserId(platformUserId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        // 1.角色关联权限
        List<Long> permissionIds = platformRolePermissionService.getPlatformPermissionIdsByRoleIds(roleIds);

        // 2.角色关联菜单,菜单关联权限
        List<Long> menuIds = platformRoleMenuService.getPlatformMenuIdsByRoleIds(roleIds);
        List<Long> upperMenuIds = platformMenuService.getPlatformUpperMenuIdsByMenuIds(menuIds);
        menuIds.addAll(upperMenuIds);
        List<Long> menuToPermissionIds = platformMenuPermissionService.getPlatformPermissionIdsByMenuIds(menuIds);
        permissionIds.addAll(menuToPermissionIds);

        if (CollectionUtils.isEmpty(permissionIds)) {
            return Lists.newArrayList();
        }

        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<PlatformPermissionEntity>()
                .select(PlatformPermissionEntity::getSymbol)
                .in(PlatformPermissionEntity::getId, permissionIds);
        return platformPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOvertPermission() {
        List<PlatformMenuEntity> menus = platformMenuService.list(new LambdaQueryWrapper<PlatformMenuEntity>()
                .select(PlatformMenuEntity::getId, PlatformMenuEntity::getRoute)
                .eq(PlatformMenuEntity::getOvert, PlatformMenuConstant.MENU_OVERT_YES));
        List<Long> menuIds = menus.stream().map(PlatformMenuEntity::getId).collect(Collectors.toList());
        List<Long> upperMenuIds = menus.stream()
                .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                .map(Long::valueOf)
                .collect(Collectors.toList());
        menuIds.addAll(upperMenuIds);
        List<Long> permissionIds = platformMenuPermissionService.getPlatformPermissionIdsByMenuIds(menuIds);
        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<PlatformPermissionEntity>()
                .select(PlatformPermissionEntity::getSymbol)
                .eq(PlatformPermissionEntity::getOvert, PlatformPermissionConstant.PERMISSION_OVERT_YES)
                .or().in(!CollectionUtils.isEmpty(permissionIds), PlatformPermissionEntity::getId, permissionIds);
        return platformPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getSubject(),p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void invalidateAllPermission() {
        // null
    }

    @Override
    public void invalidateOwnedPermission(Long platformUserId) {
        // null
    }

    @Override
    public void invalidateOvertPermission() {
        // null
    }
}
