package com.hhchun.mall.user.platform.service.impl;

import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.user.platform.constant.PlatformMenuConstant;
import com.hhchun.mall.user.platform.dao.PlatformMenuDao;
import com.hhchun.mall.user.platform.entity.dto.PlatformMenuDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;
import com.hhchun.mall.user.platform.event.Action;
import com.hhchun.mall.user.platform.event.PlatformMenuEvent;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.hhchun.mall.user.platform.entity.domain.PlatformMenuEntity;
import com.hhchun.mall.user.platform.service.PlatformMenuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("platformMenuService")
public class PlatformMenuServiceImpl extends ServiceImpl<PlatformMenuDao, PlatformMenuEntity> implements PlatformMenuService {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private PlatformMenuDao platformMenuDao;

    @Override
    public List<PlatformMenuVo> getPlatformMenuTree(@Nullable Long pid) {
        pid = Optional.ofNullable(pid).orElse(0L);
        List<PlatformMenuEntity> menus = list(new LambdaQueryWrapper<PlatformMenuEntity>().eq(PlatformMenuEntity::getPid, pid));
        return menus.stream().map(menu -> {
            PlatformMenuVo menuVo = new PlatformMenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());
    }

    @Nullable
    public PlatformMenuEntity getPlatformMenuById(Long id) {
        return getById(id);
    }

    @Nullable
    @Override
    public PlatformMenuEntity getPlatformMenuBySymbol(String symbol) {
        return getOne(new LambdaQueryWrapper<PlatformMenuEntity>()
                .eq(PlatformMenuEntity::getSymbol, symbol));
    }

    @Override
    public void savePlatformMenu(PlatformMenuDto menuDto) {
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        PlatformMenuEntity sm = getPlatformMenuBySymbol(symbol);
        Preconditions.checkCondition(sm == null, "菜单标识已存在");
        String route = PlatformMenuConstant.MENU_ROOT_ROUTE;
        if (!Objects.equals(PlatformMenuConstant.MENU_ROOT_PID, pid)) {
            PlatformMenuEntity pm = getPlatformMenuById(pid);
            Preconditions.checkCondition(pm != null, "父菜单不存在");
            route = pm.getRoute() + "/" + pm.getId();
        }
        PlatformMenuEntity menu = new PlatformMenuEntity();
        BeanUtils.copyProperties(menuDto, menu);
        menu.setRoute(route);
        save(menu);

        PlatformMenuEvent event = new PlatformMenuEvent(this, Action.SAVE,
                menu.getId(), route + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void modifyPlatformMenu(PlatformMenuDto menuDto) {
        Long id = menuDto.getId();
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        PlatformMenuEntity menu = getPlatformMenuById(id);
        Preconditions.checkCondition(menu != null, "菜单不存在");
        if (StringUtils.hasLength(symbol) && !symbol.equals(menu.getSymbol())) {
            PlatformMenuEntity sm = getPlatformMenuBySymbol(symbol);
            Preconditions.checkCondition(sm == null, "菜单标识已存在");
        }
        String route = menu.getRoute();
        if (pid != null && !pid.equals(menu.getPid())) {
            if (Objects.equals(pid, PlatformMenuConstant.MENU_ROOT_PID)) {
                route = PlatformMenuConstant.MENU_ROOT_ROUTE;
            } else {
                PlatformMenuEntity pm = getPlatformMenuById(pid);
                Preconditions.checkCondition(pm != null, "父菜单不存在");
                route = pm.getRoute() + "/" + pm.getId();
            }
            // 当前菜单的route发生改变时,子菜单的route也随着发生改变
            String oldLowerRoute = menu.getRoute() + "/" + menu.getId();
            String newLowerRoute = route + "/" + menu.getId();
            platformMenuDao.replaceRoute(oldLowerRoute, newLowerRoute);
        }
        BeanUtils.copyProperties(menuDto, menu);
        menu.setRoute(route);
        updateById(menu);

        PlatformMenuEvent event = new PlatformMenuEvent(this, Action.MODIFY,
                menu.getId(), route + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void removePlatformMenu(Long menuId) {
        PlatformMenuEntity menu = getById(menuId);
        removeById(menuId);
        String lowerRoute = menu.getRoute() + "/" + menuId;
        List<Long> lowerMenuIds = getPlatformLowerMenuIdsByRoute(lowerRoute);
        removeByIds(lowerMenuIds);
        PlatformMenuEvent event = new PlatformMenuEvent(this, Action.REMOVE,
                menuId, lowerRoute, lowerMenuIds);
        publisher.publishEvent(event);
    }

    @Override
    public List<Long> getPlatformLowerMenuIdsByRoute(String route) {
        List<PlatformMenuEntity> menus = list(new LambdaQueryWrapper<PlatformMenuEntity>()
                .select(PlatformMenuEntity::getId)
                .likeRight(PlatformMenuEntity::getRoute, route));
        return menus.stream().map(PlatformMenuEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getPlatformUpperMenuIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        List<PlatformMenuEntity> menus = list(new LambdaQueryWrapper<PlatformMenuEntity>()
                .select(PlatformMenuEntity::getRoute)
                .in(PlatformMenuEntity::getId, menuIds));
        return menus.stream()
                .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}