package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.dao.PlatformMenuPermissionDao;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuEntity;
import com.hhchun.mall.access.platform.entity.domain.PlatformPermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformMenuPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformMenuPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.access.platform.service.PlatformMenuService;
import com.hhchun.mall.access.platform.service.PlatformPermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuPermissionEntity;
import com.hhchun.mall.access.platform.service.PlatformMenuPermissionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service("platformMenuPermissionService")
public class PlatformMenuPermissionServiceImpl extends ServiceImpl<PlatformMenuPermissionDao, PlatformMenuPermissionEntity> implements PlatformMenuPermissionService {

    @Autowired
    private PlatformMenuService platformMenuService;
    @Autowired
    private PlatformPermissionService platformPermissionService;

    @Override
    @Transactional
    public void saveMenuPermissions(PlatformMenuPermissionDto menuPermissionDto) {
        Long menuId = menuPermissionDto.getMenuId();
        PlatformMenuEntity menu = platformMenuService.getPlatformMenuById(menuId);
        Set<Long> permissionIds = menuPermissionDto.getPermissionIds();
        Preconditions.checkCondition(menu != null, "菜单不存在");

        Set<Long> existPermissionIds = list(new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                .select(PlatformMenuPermissionEntity::getPermissionId)
                .eq(PlatformMenuPermissionEntity::getMenuId, menuId)
                .in(PlatformMenuPermissionEntity::getPermissionId, permissionIds))
                .stream().map(PlatformMenuPermissionEntity::getPermissionId)
                .collect(Collectors.toSet());
        // 过滤掉已存在的
        permissionIds = Sets.difference(permissionIds, existPermissionIds);

        List<PlatformMenuPermissionEntity> menuPermissions = permissionIds.stream().map(permissionId -> {
            PlatformMenuPermissionEntity menuPermission = new PlatformMenuPermissionEntity();
            menuPermission.setMenuId(menuId);
            menuPermission.setPermissionId(permissionId);
            return menuPermission;
        }).collect(Collectors.toList());
        saveBatch(menuPermissions);
    }

    @Override
    public void removeMenuPermission(Long menuPermissionId) {
        removeById(menuPermissionId);
    }

    @Override
    public PageResult<PlatformPermissionVo> getPlatformBoundPermissionList(PlatformMenuPermissionSearchDto search) {
        Long menuId = search.getMenuId();
        Set<Long> permissionIds = list(new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                .select(PlatformMenuPermissionEntity::getPermissionId)
                .eq(PlatformMenuPermissionEntity::getMenuId, menuId))
                .stream().map(PlatformMenuPermissionEntity::getPermissionId)
                .collect(Collectors.toSet());
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
    public PageResult<PlatformPermissionVo> getPlatformUnboundPermissionList(PlatformMenuPermissionSearchDto search) {
        Long menuId = search.getMenuId();
        Set<Long> permissionIds = list(new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                .select(PlatformMenuPermissionEntity::getPermissionId)
                .eq(PlatformMenuPermissionEntity::getMenuId, menuId))
                .stream().map(PlatformMenuPermissionEntity::getPermissionId)
                .collect(Collectors.toSet());
        IPage<PlatformPermissionEntity> page = platformPermissionService.page(search.getPage(), new LambdaQueryWrapper<PlatformPermissionEntity>()
                .notIn(!CollectionUtils.isEmpty(permissionIds), PlatformPermissionEntity::getId, permissionIds));
        List<PlatformPermissionEntity> permissions = page.getRecords();
        List<PlatformPermissionVo> permissionVos = permissions.stream().map(permission -> {
            PlatformPermissionVo permissionVo = new PlatformPermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, permissionVos);
    }
}