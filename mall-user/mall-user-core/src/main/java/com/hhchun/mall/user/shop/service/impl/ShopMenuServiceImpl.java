package com.hhchun.mall.user.shop.service.impl;

import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.user.shop.constant.ShopMenuConstant;
import com.hhchun.mall.user.shop.dao.ShopMenuDao;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopMenuDto;
import com.hhchun.mall.user.shop.entity.vo.ShopMenuVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopMenuEvent;
import com.hhchun.mall.user.shop.service.ShopMenuService;
import com.hhchun.mall.user.shop.service.ShopRoleMenuService;
import com.hhchun.mall.user.shop.service.ShopUserRoleService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("shopMenuService")
public class ShopMenuServiceImpl extends ServiceImpl<ShopMenuDao, ShopMenuEntity> implements ShopMenuService {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ShopMenuDao shopMenuDao;

    @Autowired
    private ShopRoleMenuService shopRoleMenuService;
    @Autowired
    private ShopUserRoleService shopUserRoleService;

    @Override
    public List<ShopMenuVo> getShopMenuTree(@Nullable Long pid) {
        pid = Optional.ofNullable(pid).orElse(0L);
        List<ShopMenuEntity> menus = list(new LambdaQueryWrapper<ShopMenuEntity>().eq(ShopMenuEntity::getPid, pid));
        return menus.stream().map(menu -> {
            ShopMenuVo menuVo = new ShopMenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());
    }

    @Nullable
    public ShopMenuEntity getShopMenuById(Long id) {
        return getById(id);
    }

    @Nullable
    @Override
    public ShopMenuEntity getShopMenuBySymbol(String symbol) {
        return getOne(new LambdaQueryWrapper<ShopMenuEntity>()
                .eq(ShopMenuEntity::getSymbol, symbol));
    }

    @Override
    public void saveShopMenu(ShopMenuDto menuDto) {
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        ShopMenuEntity sm = getShopMenuBySymbol(symbol);
        Preconditions.checkCondition(sm == null, "菜单标识已存在");
        String route = ShopMenuConstant.MENU_ROOT_ROUTE;
        if (!Objects.equals(ShopMenuConstant.MENU_ROOT_PID, pid)) {
            ShopMenuEntity pm = getShopMenuById(pid);
            Preconditions.checkCondition(pm != null, "父菜单不存在");
            route = pm.getRoute() + "/" + pm.getId();
        }
        ShopMenuEntity menu = new ShopMenuEntity();
        BeanUtils.copyProperties(menuDto, menu);
        menu.setRoute(route);
        save(menu);

        ShopMenuEvent event = new ShopMenuEvent(this, Action.SAVE,
                menu.getId(), route + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void modifyShopMenu(ShopMenuDto menuDto) {
        Long id = menuDto.getId();
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        ShopMenuEntity menu = getShopMenuById(id);
        Preconditions.checkCondition(menu != null, "菜单不存在");
        if (StringUtils.hasLength(symbol) && !symbol.equals(menu.getSymbol())) {
            ShopMenuEntity sm = getShopMenuBySymbol(symbol);
            Preconditions.checkCondition(sm == null, "菜单标识已存在");
        }
        String route = menu.getRoute();
        if (pid != null && !pid.equals(menu.getPid())) {
            if (Objects.equals(pid, ShopMenuConstant.MENU_ROOT_PID)) {
                route = ShopMenuConstant.MENU_ROOT_ROUTE;
            } else {
                ShopMenuEntity pm = getShopMenuById(pid);
                Preconditions.checkCondition(pm != null, "父菜单不存在");
                route = pm.getRoute() + "/" + pm.getId();
            }
            // 当前菜单的route发生改变时,子菜单的route也随着发生改变
            String oldLowerRoute = menu.getRoute() + "/" + menu.getId();
            String newLowerRoute = route + "/" + menu.getId();
            shopMenuDao.replaceRoute(oldLowerRoute, newLowerRoute);
        }
        BeanUtils.copyProperties(menuDto, menu);
        menu.setRoute(route);
        updateById(menu);

        ShopMenuEvent event = new ShopMenuEvent(this, Action.MODIFY,
                menu.getId(), route + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void removeShopMenu(Long menuId) {
        ShopMenuEntity menu = getById(menuId);
        removeById(menuId);
        String lowerRoute = menu.getRoute() + "/" + menuId;
        List<Long> lowerMenuIds = getShopLowerMenuIdsByRoute(lowerRoute);
        removeByIds(lowerMenuIds);
        ShopMenuEvent event = new ShopMenuEvent(this, Action.REMOVE,
                menuId, lowerRoute, lowerMenuIds);
        publisher.publishEvent(event);
    }

    @Override
    public List<Long> getShopLowerMenuIdsByRoute(String route) {
        List<ShopMenuEntity> menus = list(new LambdaQueryWrapper<ShopMenuEntity>()
                .select(ShopMenuEntity::getId)
                .likeRight(ShopMenuEntity::getRoute, route));
        return menus.stream().map(ShopMenuEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getShopUpperMenuIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        List<ShopMenuEntity> menus = list(new LambdaQueryWrapper<ShopMenuEntity>()
                .select(ShopMenuEntity::getRoute)
                .in(ShopMenuEntity::getId, menuIds));
        return menus.stream()
                .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShopMenuVo> getShopMenusByUserId(Long userId, boolean upper, boolean combine) {
        List<Long> roleIds = shopUserRoleService.getShopRoleIdsByUserId(userId);
        List<Long> menuIds = shopRoleMenuService.getShopMenuIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        List<ShopMenuEntity> menus = list(new LambdaQueryWrapper<ShopMenuEntity>()
                .in(ShopMenuEntity::getId, menuIds));
        if (upper) {
            List<Long> upperMenuIds = menus.stream()
                    .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            List<ShopMenuEntity> upperMenus = list(new LambdaQueryWrapper<ShopMenuEntity>()
                    .in(ShopMenuEntity::getId, upperMenuIds));
            menus.addAll(upperMenus);
        }
        List<ShopMenuVo> mvs = menus.stream().map(m -> {
            ShopMenuVo mv = new ShopMenuVo();
            BeanUtils.copyProperties(m, mv);
            return mv;
        }).collect(Collectors.toList());
        if (combine && upper) {
            return ShopMenuVo.combine(mvs);
        } else {
            return mvs;
        }
    }
}