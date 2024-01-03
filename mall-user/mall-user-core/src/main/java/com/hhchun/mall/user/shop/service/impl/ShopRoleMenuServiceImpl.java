package com.hhchun.mall.user.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.user.shop.constant.ShopOtherConstant;
import com.hhchun.mall.user.shop.dao.ShopRoleMenuDao;
import com.hhchun.mall.user.shop.entity.bo.ShopMenuBo;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleEntity;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleMenuEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopRoleMenuDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleMenuSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopMenuVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopMenuEvent;
import com.hhchun.mall.user.shop.event.ShopRoleEvent;
import com.hhchun.mall.user.shop.event.ShopRoleMenuEvent;
import com.hhchun.mall.user.shop.service.ShopRoleMenuService;
import com.hhchun.mall.user.shop.service.ShopRoleService;
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


@Service("shopRoleMenuService")
public class ShopRoleMenuServiceImpl extends ServiceImpl<ShopRoleMenuDao, ShopRoleMenuEntity> implements ShopRoleMenuService {

    @Autowired
    private ShopRoleService shopRoleService;
    @Autowired
    private ShopRoleMenuDao shopRoleMenuDao;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveShopRoleMenus(ShopRoleMenuDto roleMenuDto) {
        Long roleId = roleMenuDto.getRoleId();
        List<ShopRoleMenuDto.Menu> menus = roleMenuDto.getMenus();
        ShopRoleEntity role = shopRoleService.getShopRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<Long> unboundMenuIds = menus.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(ShopRoleMenuDto.Menu::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundMenuIds)) {
            remove(new LambdaQueryWrapper<ShopRoleMenuEntity>()
                    .eq(ShopRoleMenuEntity::getRoleId, roleId)
                    .in(ShopRoleMenuEntity::getMenuId, unboundMenuIds));
        }

        List<Long> boundMenuIds = menus.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(ShopRoleMenuDto.Menu::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundMenuIds)) {
            List<ShopRoleMenuEntity> roleMenus = boundMenuIds.stream().map(menuId -> {
                ShopRoleMenuEntity roleMenu = new ShopRoleMenuEntity();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            saveBatch(roleMenus);
        }

        publisher.publishEvent(new ShopRoleMenuEvent(this, Action.SAVE, roleId));

    }

    @Override
    public List<ShopMenuVo> getShopMenuTree(ShopRoleMenuSearchDto search) {
        List<ShopMenuBo> mbs = shopRoleMenuDao.getShopMenuTree(search);
        return mbs.stream().map(mb -> {
            ShopMenuVo mv = new ShopMenuVo();
            BeanUtils.copyProperties(mb, mv);
            return mv;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getShopRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ShopRoleMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ShopRoleMenuEntity::getRoleId);
        wrapper.in(ShopRoleMenuEntity::getMenuId, menuIds);
        return list(wrapper).stream()
                .map(ShopRoleMenuEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getShopRemovedRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        return shopRoleMenuDao.getShopRemovedRoleIdsByMenuIds(menuIds);
    }

    @Override
    public List<Long> getShopMenuIdsByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ShopRoleMenuEntity> wrapper = new LambdaQueryWrapper<ShopRoleMenuEntity>()
                .select(ShopRoleMenuEntity::getMenuId)
                .in(ShopRoleMenuEntity::getRoleId, roleIds);
        return list(wrapper)
                .stream().map(ShopRoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopRoleEvent(ShopRoleEvent event) {
        remove(new LambdaQueryWrapper<ShopRoleMenuEntity>()
                .eq(ShopRoleMenuEntity::getRoleId, event.getRoleId()));
    }

    @Transactional
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopMenuEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopMenuEvent(ShopMenuEvent event) {
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(event.getMenuId());
        menuIds.addAll(event.getLowerMenuIds());
        remove(new LambdaQueryWrapper<ShopRoleMenuEntity>()
                .in(ShopRoleMenuEntity::getMenuId, menuIds));
    }
}