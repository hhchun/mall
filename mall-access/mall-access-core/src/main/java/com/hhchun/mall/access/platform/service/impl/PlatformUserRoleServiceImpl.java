package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Sets;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.dao.PlatformUserRoleDao;
import com.hhchun.mall.access.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.access.platform.entity.domain.PlatformUserEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformUserRoleDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformUserRoleSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformRoleVo;
import com.hhchun.mall.access.platform.service.PlatformRoleService;
import com.hhchun.mall.access.platform.service.PlatformUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.entity.domain.PlatformUserRoleEntity;
import com.hhchun.mall.access.platform.service.PlatformUserRoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("platformUserRoleService")
public class PlatformUserRoleServiceImpl extends ServiceImpl<PlatformUserRoleDao, PlatformUserRoleEntity> implements PlatformUserRoleService {

    @Autowired
    private PlatformUserService platformUserService;
    @Autowired
    private PlatformRoleService platformRoleService;

    @Transactional
    @Override
    public void savePlatformUserRoles(PlatformUserRoleDto userRoleDto) {
        Long userId = userRoleDto.getUserId();
        Set<Long> roleIds = userRoleDto.getRoleIds();

        Set<Long> existRoleIds = list(new LambdaQueryWrapper<PlatformUserRoleEntity>()
                .select(PlatformUserRoleEntity::getRoleId)
                .eq(PlatformUserRoleEntity::getUserId, userId)
                .in(PlatformUserRoleEntity::getRoleId, roleIds))
                .stream().map(PlatformUserRoleEntity::getRoleId)
                .collect(Collectors.toSet());
        // 过滤掉已存在的
        roleIds = Sets.difference(roleIds, existRoleIds);

        PlatformUserEntity platformUser = platformUserService.getPlatformUserById(userId);
        Preconditions.checkCondition(platformUser != null, "用户不存在");
        List<PlatformUserRoleEntity> userRoles = roleIds.stream().map(roleId -> {
            PlatformUserRoleEntity userRole = new PlatformUserRoleEntity();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        saveBatch(userRoles);
    }

    @Override
    public void removePlatformUserRole(Long userRoleId) {
        removeById(userRoleId);
    }

    @Override
    public PageResult<PlatformRoleVo> getPlatformBoundRoleList(PlatformUserRoleSearchDto search) {
        Long userId = search.getUserId();
        List<PlatformUserRoleEntity> userRoles = list(new LambdaQueryWrapper<PlatformUserRoleEntity>()
                .select(PlatformUserRoleEntity::getRoleId)
                .eq(PlatformUserRoleEntity::getUserId, userId));
        List<Long> roleIds = userRoles.stream().map(PlatformUserRoleEntity::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return PageResult.empty();
        }
        IPage<PlatformRoleEntity> page = platformRoleService.page(search.getPage(), new LambdaQueryWrapper<PlatformRoleEntity>()
                .in(PlatformRoleEntity::getId, roleIds));
        List<PlatformRoleEntity> roles = page.getRecords();
        List<PlatformRoleVo> roleVos = roles.stream().map(role -> {
            PlatformRoleVo roleVo = new PlatformRoleVo();
            BeanUtils.copyProperties(role, roleVo);
            return roleVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, roleVos);
    }

    @Override
    public PageResult<PlatformRoleVo> getPlatformUnboundRoleList(PlatformUserRoleSearchDto search) {
        Long userId = search.getUserId();
        List<PlatformUserRoleEntity> userRoles = list(new LambdaQueryWrapper<PlatformUserRoleEntity>()
                .select(PlatformUserRoleEntity::getRoleId)
                .eq(PlatformUserRoleEntity::getUserId, userId));
        List<Long> roleIds = userRoles.stream().map(PlatformUserRoleEntity::getRoleId).collect(Collectors.toList());
        IPage<PlatformRoleEntity> page = platformRoleService.page(search.getPage(), new LambdaQueryWrapper<PlatformRoleEntity>()
                .notIn(!CollectionUtils.isEmpty(roleIds), PlatformRoleEntity::getId, roleIds));
        List<PlatformRoleEntity> roles = page.getRecords();
        List<PlatformRoleVo> roleVos = roles.stream().map(role -> {
            PlatformRoleVo roleVo = new PlatformRoleVo();
            BeanUtils.copyProperties(role, roleVo);
            return roleVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, roleVos);
    }
}