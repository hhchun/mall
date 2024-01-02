package com.hhchun.mall.user.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.constant.PlatformOtherConstant;
import com.hhchun.mall.user.platform.dao.PlatformRolePermissionDao;
import com.hhchun.mall.user.platform.entity.bo.PlatformPermissionBo;
import com.hhchun.mall.user.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.user.platform.entity.domain.PlatformRolePermissionEntity;
import com.hhchun.mall.user.platform.entity.dto.PlatformRolePermissionDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.user.platform.event.Action;
import com.hhchun.mall.user.platform.event.PlatformPermissionEvent;
import com.hhchun.mall.user.platform.event.PlatformRoleEvent;
import com.hhchun.mall.user.platform.event.PlatformRolePermissionEvent;
import com.hhchun.mall.user.platform.service.PlatformRolePermissionService;
import com.hhchun.mall.user.platform.service.PlatformRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("platformRolePermissionService")
public class PlatformRolePermissionServiceImpl extends ServiceImpl<PlatformRolePermissionDao, PlatformRolePermissionEntity> implements PlatformRolePermissionService {

    @Autowired
    private PlatformRoleService platformRoleService;
    @Autowired
    private PlatformRolePermissionDao platformRolePermissionDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void savePlatformRolePermissions(PlatformRolePermissionDto rolePermissionDto) {
        Long roleId = rolePermissionDto.getRoleId();
        List<PlatformRolePermissionDto.Permission> permissions = rolePermissionDto.getPermissions();
        PlatformRoleEntity role = platformRoleService.getPlatformRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<Long> unboundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(PlatformRolePermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundPermissionIds)) {
            remove(new LambdaQueryWrapper<PlatformRolePermissionEntity>()
                    .eq(PlatformRolePermissionEntity::getRoleId, roleId)
                    .in(PlatformRolePermissionEntity::getPermissionId, unboundPermissionIds));
        }

        List<Long> boundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(PlatformRolePermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundPermissionIds)) {
            List<PlatformRolePermissionEntity> rolePermissions = boundPermissionIds.stream().map(permissionId -> {
                PlatformRolePermissionEntity rolePermission = new PlatformRolePermissionEntity();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }

        publisher.publishEvent(new PlatformRolePermissionEvent(this, Action.SAVE, roleId));
    }

    @Override
    public PageResult<PlatformPermissionVo> getPlatformPermissions(PlatformRolePermissionSearchDto search) {
        IPage<PlatformPermissionBo> page = platformRolePermissionDao.getPlatformPermissions(search.getPage(), search);
        List<PlatformPermissionVo> pvs = page.getRecords().stream().map(pb -> {
            PlatformPermissionVo pv = new PlatformPermissionVo();
            BeanUtils.copyProperties(pb, pv);
            return pv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, pvs);
    }

    @Override
    public List<Long> getPlatformRoleIdsByPermissionId(Long permissionId) {
        LambdaQueryWrapper<PlatformRolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformRolePermissionEntity::getRoleId);
        wrapper.eq(PlatformRolePermissionEntity::getPermissionId, permissionId);
        return list(wrapper).stream()
                .map(PlatformRolePermissionEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<Long> getPlatformRemovedRoleIdsByPermissionId(Long permissionId) {
        return platformRolePermissionDao.getPlatformRemovedRoleIdsByPermissionId(permissionId);
    }

    @Nonnull
    @Override
    public List<Long> getPlatformPermissionIdsByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<PlatformRolePermissionEntity> wrapper = new LambdaQueryWrapper<PlatformRolePermissionEntity>()
                .select(PlatformRolePermissionEntity::getPermissionId)
                .in(PlatformRolePermissionEntity::getRoleId, roleIds);
        return list(wrapper)
                .stream()
                .map(PlatformRolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformRoleEvent(PlatformRoleEvent event) {
        remove(new LambdaQueryWrapper<PlatformRolePermissionEntity>()
                .eq(PlatformRolePermissionEntity::getRoleId, event.getRoleId()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformPermissionEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformPermissionEvent(PlatformPermissionEvent event) {
        remove(new LambdaQueryWrapper<PlatformRolePermissionEntity>()
                .eq(PlatformRolePermissionEntity::getPermissionId, event.getPermissionId()));
    }
}