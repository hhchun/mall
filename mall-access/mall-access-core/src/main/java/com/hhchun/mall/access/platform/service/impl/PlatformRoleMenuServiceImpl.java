package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.platform.constant.PlatformOtherConstant;
import com.hhchun.mall.access.platform.dao.PlatformRoleMenuDao;
import com.hhchun.mall.access.platform.entity.bo.PlatformMenuBo;
import com.hhchun.mall.access.platform.entity.domain.*;
import com.hhchun.mall.access.platform.entity.dto.PlatformRoleMenuDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformMenuVo;
import com.hhchun.mall.access.platform.event.Action;
import com.hhchun.mall.access.platform.event.PlatformMenuEvent;
import com.hhchun.mall.access.platform.event.PlatformRoleEvent;
import com.hhchun.mall.access.platform.event.PlatformRoleMenuEvent;
import com.hhchun.mall.access.platform.service.*;
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


@Service("platformRoleMenuService")
public class PlatformRoleMenuServiceImpl extends ServiceImpl<PlatformRoleMenuDao, PlatformRoleMenuEntity> implements PlatformRoleMenuService {

    @Autowired
    private PlatformRoleService platformRoleService;
    @Autowired
    private PlatformRoleMenuDao platformRoleMenuDao;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void savePlatformRoleMenus(PlatformRoleMenuDto roleMenuDto) {
        Long roleId = roleMenuDto.getRoleId();
        List<PlatformRoleMenuDto.Menu> menus = roleMenuDto.getMenus();
        PlatformRoleEntity role = platformRoleService.getPlatformRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<Long> unboundMenuIds = menus.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(PlatformRoleMenuDto.Menu::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundMenuIds)) {
            remove(new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                    .eq(PlatformRoleMenuEntity::getRoleId, roleId)
                    .in(PlatformRoleMenuEntity::getMenuId, unboundMenuIds));
        }

        List<Long> boundMenuIds = menus.stream()
                .filter(p -> Objects.equals(p.getAction(), PlatformOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(PlatformRoleMenuDto.Menu::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundMenuIds)) {
            List<PlatformRoleMenuEntity> roleMenus = boundMenuIds.stream().map(menuId -> {
                PlatformRoleMenuEntity roleMenu = new PlatformRoleMenuEntity();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            saveBatch(roleMenus);
        }

        publisher.publishEvent(new PlatformRoleMenuEvent(this, Action.SAVE, roleId));

    }

    @Override
    public List<PlatformMenuVo> getPlatformMenuTree(PlatformRoleMenuSearchDto search) {
        List<PlatformMenuBo> mbs = platformRoleMenuDao.getPlatformMenuTree(search);
        return mbs.stream().map(mb -> {
            PlatformMenuVo mv = new PlatformMenuVo();
            BeanUtils.copyProperties(mb, mv);
            return mv;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getPlatformRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<PlatformRoleMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformRoleMenuEntity::getRoleId);
        wrapper.in(PlatformRoleMenuEntity::getMenuId, menuIds);
        return list(wrapper).stream()
                .map(PlatformRoleMenuEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getPlatformRemovedRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        return platformRoleMenuDao.getPlatformRemovedRoleIdsByMenuIds(menuIds);
    }

    @Override
    public List<Long> getPlatformMenuIdsByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<PlatformRoleMenuEntity> wrapper = new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .select(PlatformRoleMenuEntity::getMenuId)
                .in(PlatformRoleMenuEntity::getRoleId, roleIds);
        return list(wrapper)
                .stream().map(PlatformRoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformRoleEvent(PlatformRoleEvent event) {
        remove(new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .eq(PlatformRoleMenuEntity::getRoleId, event.getRoleId()));
    }

    @Transactional
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = PlatformMenuEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedPlatformMenuEvent(PlatformMenuEvent event) {
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(event.getMenuId());
        menuIds.addAll(event.getLowerMenuIds());
        remove(new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .in(PlatformRoleMenuEntity::getMenuId, menuIds));
    }
}