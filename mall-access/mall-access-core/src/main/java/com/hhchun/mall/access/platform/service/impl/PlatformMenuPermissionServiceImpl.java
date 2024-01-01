package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.constant.PlatformOtherConstant;
import com.hhchun.mall.access.platform.dao.PlatformMenuPermissionDao;
import com.hhchun.mall.access.platform.entity.bo.PlatformPermissionBo;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuEntity;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuPermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformMenuPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformMenuPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.access.platform.event.Action;
import com.hhchun.mall.access.platform.event.PlatformMenuEvent;
import com.hhchun.mall.access.platform.event.PlatformMenuPermissionEvent;
import com.hhchun.mall.access.platform.event.PlatformPermissionEvent;
import com.hhchun.mall.access.platform.service.PlatformMenuPermissionService;
import com.hhchun.mall.access.platform.service.PlatformMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("platformMenuPermissionService")
public class PlatformMenuPermissionServiceImpl extends ServiceImpl<PlatformMenuPermissionDao, PlatformMenuPermissionEntity> implements PlatformMenuPermissionService {

    @Autowired
    private PlatformMenuService platformMenuService;
    @Autowired
    private PlatformMenuPermissionDao platformMenuPermissionDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveMenuPermissions(PlatformMenuPermissionDto menuPermissionDto) {
        Long menuId = menuPermissionDto.getMenuId();
        PlatformMenuEntity menu = platformMenuService.getPlatformMenuById(menuId);
        List<PlatformMenuPermissionDto.Permission> permissions = menuPermissionDto.getPermissions();
        Preconditions.checkCondition(menu != null, "菜单不存在");

        List<Long> unboundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(PlatformMenuPermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundPermissionIds)) {
            remove(new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                    .eq(PlatformMenuPermissionEntity::getMenuId, menuId)
                    .in(PlatformMenuPermissionEntity::getPermissionId, unboundPermissionIds));
        }

        List<Long> boundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(PlatformMenuPermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundPermissionIds)) {
            List<PlatformMenuPermissionEntity> rolePermissions = boundPermissionIds.stream().map(permissionId -> {
                PlatformMenuPermissionEntity menuPermission = new PlatformMenuPermissionEntity();
                menuPermission.setMenuId(menuId);
                menuPermission.setPermissionId(permissionId);
                return menuPermission;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }
        PlatformMenuPermissionEvent event = new PlatformMenuPermissionEvent(this, Action.SAVE,
                menuId, menu.getRoute() + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    public PageResult<PlatformPermissionVo> getPlatformPermissions(PlatformMenuPermissionSearchDto search) {
        IPage<PlatformPermissionBo> page = platformMenuPermissionDao.getPlatformPermissions(search.getPage(), search);
        List<PlatformPermissionVo> pvs = page.getRecords().stream().map(pb -> {
            PlatformPermissionVo pv = new PlatformPermissionVo();
            BeanUtils.copyProperties(pb, pv);
            return pv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, pvs);
    }

    @Override
    public List<Long> getPlatformPermissionIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<PlatformMenuPermissionEntity> wrapper = new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                .select(PlatformMenuPermissionEntity::getPermissionId)
                .in(PlatformMenuPermissionEntity::getMenuId, menuIds);
        return list(wrapper)
                .stream().map(PlatformMenuPermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformPermissionEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformPermissionEvent(PlatformPermissionEvent event) {
        remove(new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                .eq(PlatformMenuPermissionEntity::getPermissionId, event.getPermissionId()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformMenuEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformMenuEvent(PlatformMenuEvent event) {
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(event.getMenuId());
        menuIds.addAll(event.getLowerMenuIds());
        remove(new LambdaQueryWrapper<PlatformMenuPermissionEntity>()
                .in(PlatformMenuPermissionEntity::getMenuId, menuIds));
    }
}