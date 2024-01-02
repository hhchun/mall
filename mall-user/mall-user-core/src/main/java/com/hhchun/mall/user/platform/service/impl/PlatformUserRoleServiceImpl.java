package com.hhchun.mall.user.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.constant.PlatformOtherConstant;
import com.hhchun.mall.user.platform.dao.PlatformUserRoleDao;
import com.hhchun.mall.user.platform.entity.bo.PlatformRoleBo;
import com.hhchun.mall.user.platform.entity.dto.PlatformUserRoleDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformUserRoleSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformRoleVo;
import com.hhchun.mall.user.platform.event.Action;
import com.hhchun.mall.user.platform.event.PlatformRoleEvent;
import com.hhchun.mall.user.platform.event.PlatformUserRoleEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.user.platform.entity.domain.PlatformUserRoleEntity;
import com.hhchun.mall.user.platform.service.PlatformUserRoleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("platformUserRoleService")
public class PlatformUserRoleServiceImpl extends ServiceImpl<PlatformUserRoleDao, PlatformUserRoleEntity> implements PlatformUserRoleService {
    @Autowired
    private PlatformUserRoleDao platformUserRoleDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void savePlatformUserRoles(PlatformUserRoleDto userRoleDto) {
        Long userId = userRoleDto.getUserId();
        List<PlatformUserRoleDto.Role> roles = userRoleDto.getRoles();

        List<Long> unboundRoleIds = roles.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(PlatformUserRoleDto.Role::getRoleId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundRoleIds)) {
            remove(new LambdaQueryWrapper<PlatformUserRoleEntity>()
                    .eq(PlatformUserRoleEntity::getUserId, userId)
                    .in(PlatformUserRoleEntity::getRoleId, unboundRoleIds));
        }

        List<Long> boundRoleIds = roles.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(PlatformUserRoleDto.Role::getRoleId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundRoleIds)) {
            List<PlatformUserRoleEntity> rolePermissions = boundRoleIds.stream().map(roleId -> {
                PlatformUserRoleEntity userRole = new PlatformUserRoleEntity();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }

        publisher.publishEvent(new PlatformUserRoleEvent(this, Action.SAVE, userId));
    }

    @Override
    public PageResult<PlatformRoleVo> getPlatformRoles(PlatformUserRoleSearchDto search) {
        IPage<PlatformRoleBo> page = platformUserRoleDao.getPlatformRoles(search.getPage(), search);
        List<PlatformRoleVo> rvs = page.getRecords().stream().map(rb -> {
            PlatformRoleVo rv = new PlatformRoleVo();
            BeanUtils.copyProperties(rb, rv);
            return rv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, rvs);
    }

    @Override
    public List<Long> getPlatformUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<PlatformUserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformUserRoleEntity::getUserId);
        wrapper.in(PlatformUserRoleEntity::getRoleId, roleIds);
        return list(wrapper).stream()
                .map(PlatformUserRoleEntity::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getPlatformRemovedUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        return platformUserRoleDao.getPlatformRemovedUserIdsByRoleIds(roleIds);
    }

    @Override
    public List<Long> getPlatformRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<PlatformUserRoleEntity> userRoleWrapper = new LambdaQueryWrapper<PlatformUserRoleEntity>()
                .select(PlatformUserRoleEntity::getRoleId)
                .eq(PlatformUserRoleEntity::getUserId, userId);
        return list(userRoleWrapper)
                .stream()
                .map(PlatformUserRoleEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformRoleEvent(PlatformRoleEvent event) {
        remove(new LambdaQueryWrapper<PlatformUserRoleEntity>()
                .eq(PlatformUserRoleEntity::getRoleId, event.getRoleId()));
    }


}