package com.hhchun.mall.access.platform.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.dao.PlatformRolePermissionDao;
import com.hhchun.mall.access.platform.entity.domain.PlatformPermissionEntity;
import com.hhchun.mall.access.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.access.platform.entity.domain.PlatformRolePermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.PlatformRolePermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.access.platform.service.PlatformPermissionService;
import com.hhchun.mall.access.platform.service.PlatformRolePermissionService;
import com.hhchun.mall.access.platform.service.PlatformRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("platformRolePermissionService")
public class PlatformRolePermissionServiceImpl extends ServiceImpl<PlatformRolePermissionDao, PlatformRolePermissionEntity> implements PlatformRolePermissionService {

    @Autowired
    private PlatformRoleService platformRoleService;
    @Autowired
    private PlatformPermissionService platformPermissionService;

    @Override
    @Transactional
    public void savePlatformRolePermissions(PlatformRolePermissionDto rolePermissionDto) {
        Long roleId = rolePermissionDto.getRoleId();
        List<Long> permissionIds = rolePermissionDto.getPermissionIds();
        Preconditions.checkArgument(!CollectionUtils.isEmpty(permissionIds), "没有选择权限");
        PlatformRoleEntity role = platformRoleService.getPlatformRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<PlatformRolePermissionEntity> rolePermissions = permissionIds.stream().map(permissionId -> {
            PlatformRolePermissionEntity rolePermission = new PlatformRolePermissionEntity();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            return rolePermission;
        }).collect(Collectors.toList());

        saveBatch(rolePermissions);
    }

    @Override
    public void removePlatformRolePermission(Long rolePermissionId) {
        removeById(rolePermissionId);
    }

    @Override
    public PageResult<PlatformPermissionVo> getPlatformBoundPermissionList(PlatformRolePermissionSearchDto search) {
        Long roleId = search.getRoleId();
        LambdaQueryWrapper<PlatformRolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformRolePermissionEntity::getId, PlatformRolePermissionEntity::getPermissionId);
        wrapper.eq(PlatformRolePermissionEntity::getRoleId, roleId);
        List<PlatformRolePermissionEntity> rolePermissions = list(wrapper);
        List<Long> permissionIds = rolePermissions.stream().map(PlatformRolePermissionEntity::getPermissionId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permissionIds)) {
            return PageResult.empty();
        }
        IPage<PlatformPermissionEntity> page = platformPermissionService.page(search.getPage(), new LambdaQueryWrapper<PlatformPermissionEntity>()
                .in(PlatformPermissionEntity::getId, permissionIds));

        List<PlatformPermissionEntity> permissions = page.getRecords();
        List<PlatformPermissionVo> permissionVos = permissions.stream().map(permission -> {
            PlatformPermissionVo permissionVo = new PlatformPermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, permissionVos);
    }

    @Override
    public PageResult<PlatformPermissionVo> getPlatformUnboundPermissionList(PlatformRolePermissionSearchDto search) {
        Long roleId = search.getRoleId();
        LambdaQueryWrapper<PlatformRolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformRolePermissionEntity::getId, PlatformRolePermissionEntity::getPermissionId);
        wrapper.eq(PlatformRolePermissionEntity::getRoleId, roleId);
        List<PlatformRolePermissionEntity> rolePermissions = list(wrapper);
        List<Long> permissionIds = rolePermissions.stream().map(PlatformRolePermissionEntity::getPermissionId).collect(Collectors.toList());

        IPage<PlatformPermissionEntity> page = platformPermissionService.page(search.getPage(), new LambdaQueryWrapper<PlatformPermissionEntity>()
                .notIn(!CollectionUtils.isEmpty(permissionIds),PlatformPermissionEntity::getId, permissionIds));
        List<PlatformPermissionEntity> permissions = page.getRecords();
        List<PlatformPermissionVo> permissionVos = permissions.stream().map(permission -> {
            PlatformPermissionVo permissionVo = new PlatformPermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, permissionVos);
    }
}