package com.hhchun.mall.access.platform.provider.cache;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hhchun.mall.access.platform.entity.domain.*;
import com.hhchun.mall.access.platform.service.*;
import com.hhchun.mall.access.support.provider.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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

    @Override
    public List<Permission> getAllPermission() {
        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformPermissionEntity::getSymbol, PlatformPermissionEntity::getSubject);
        return platformPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getId(), p.getSubject())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOwnedPermission(Long platformUserId) {
        LambdaQueryWrapper<PlatformUserRoleEntity> userRoleWrapper = new LambdaQueryWrapper<PlatformUserRoleEntity>()
                .select(PlatformUserRoleEntity::getRoleId)
                .eq(PlatformUserRoleEntity::getUserId, platformUserId);
        List<Long> roleIds = platformUserRoleService.list(userRoleWrapper)
                .stream()
                .map(PlatformUserRoleEntity::getRoleId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 1.角色关联权限
        LambdaQueryWrapper<PlatformRolePermissionEntity> rolePermissionWrapper = new LambdaQueryWrapper<PlatformRolePermissionEntity>()
                .select(PlatformRolePermissionEntity::getPermissionId)
                .in(PlatformRolePermissionEntity::getRoleId, roleIds);
        List<Long> permissionIds = platformRolePermissionService.list(rolePermissionWrapper)
                .stream()
                .map(PlatformRolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());

        // 2.角色关联菜单,菜单关联权限
        LambdaQueryWrapper<PlatformRoleMenuEntity> roleMenuWrapper = new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .select(PlatformRoleMenuEntity::getMenuId)
                .in(PlatformRoleMenuEntity::getRoleId, roleIds);
        List<Long> menuIds = platformRoleMenuService.list(roleMenuWrapper)
                .stream().map(PlatformRoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(menuIds)) {
            LambdaQueryWrapper<PlatformMenuPermissionEntity> menuPermissionWrapper = new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                    .select(PlatformMenuPermissionEntity::getPermissionId)
                    .in(PlatformMenuPermissionEntity::getMenuId, menuIds);
            List<Long> menuToPermissionIds = platformMenuPermissionService.list(menuPermissionWrapper)
                    .stream().map(PlatformMenuPermissionEntity::getPermissionId)
                    .collect(Collectors.toList());
            // 追加
            permissionIds.addAll(menuToPermissionIds);
        }
        if (CollectionUtils.isEmpty(permissionIds)) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformPermissionEntity::getSymbol);
        wrapper.in(PlatformPermissionEntity::getId, permissionIds);
        return platformPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getId(), p.getSubject())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOvertPermission() {
        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformPermissionEntity::getSymbol);
        wrapper.eq(PlatformPermissionEntity::getOvert, true);
        return platformPermissionService.list(wrapper).stream()
                .map(p -> new Permission(p.getSymbol(), new Permission.Extra(p.getId(), p.getSubject())))
                .collect(Collectors.toList());
    }
}
