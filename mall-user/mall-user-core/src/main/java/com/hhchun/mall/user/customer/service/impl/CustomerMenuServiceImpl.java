package com.hhchun.mall.user.customer.service.impl;

import cn.hutool.core.text.StrSplitter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.user.customer.constant.CustomerMenuConstant;
import com.hhchun.mall.user.customer.dao.CustomerMenuDao;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerMenuDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerMenuVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerMenuEvent;
import com.hhchun.mall.user.customer.service.CustomerMenuService;
import com.hhchun.mall.user.customer.service.CustomerRoleMenuService;
import com.hhchun.mall.user.customer.service.CustomerUserRoleService;
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


@Service("customerMenuService")
public class CustomerMenuServiceImpl extends ServiceImpl<CustomerMenuDao, CustomerMenuEntity> implements CustomerMenuService {

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private CustomerMenuDao customerMenuDao;

    @Autowired
    private CustomerRoleMenuService customerRoleMenuService;
    @Autowired
    private CustomerUserRoleService customerUserRoleService;

    @Override
    public List<CustomerMenuVo> getCustomerMenuTree(@Nullable Long pid) {
        pid = Optional.ofNullable(pid).orElse(0L);
        List<CustomerMenuEntity> menus = list(new LambdaQueryWrapper<CustomerMenuEntity>().eq(CustomerMenuEntity::getPid, pid));
        return menus.stream().map(menu -> {
            CustomerMenuVo menuVo = new CustomerMenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());
    }

    @Nullable
    public CustomerMenuEntity getCustomerMenuById(Long id) {
        return getById(id);
    }

    @Nullable
    @Override
    public CustomerMenuEntity getCustomerMenuBySymbol(String symbol) {
        return getOne(new LambdaQueryWrapper<CustomerMenuEntity>()
                .eq(CustomerMenuEntity::getSymbol, symbol));
    }

    @Override
    public void saveCustomerMenu(CustomerMenuDto menuDto) {
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        CustomerMenuEntity sm = getCustomerMenuBySymbol(symbol);
        Preconditions.checkCondition(sm == null, "菜单标识已存在");
        String route = CustomerMenuConstant.MENU_ROOT_ROUTE;
        if (!Objects.equals(CustomerMenuConstant.MENU_ROOT_PID, pid)) {
            CustomerMenuEntity pm = getCustomerMenuById(pid);
            Preconditions.checkCondition(pm != null, "父菜单不存在");
            route = pm.getRoute() + "/" + pm.getId();
        }
        CustomerMenuEntity menu = new CustomerMenuEntity();
        BeanUtils.copyProperties(menuDto, menu);
        menu.setRoute(route);
        save(menu);

        CustomerMenuEvent event = new CustomerMenuEvent(this, Action.SAVE,
                menu.getId(), route + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void modifyCustomerMenu(CustomerMenuDto menuDto) {
        Long id = menuDto.getId();
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        CustomerMenuEntity menu = getCustomerMenuById(id);
        Preconditions.checkCondition(menu != null, "菜单不存在");
        if (StringUtils.hasLength(symbol) && !symbol.equals(menu.getSymbol())) {
            CustomerMenuEntity sm = getCustomerMenuBySymbol(symbol);
            Preconditions.checkCondition(sm == null, "菜单标识已存在");
        }
        String route = menu.getRoute();
        if (pid != null && !pid.equals(menu.getPid())) {
            if (Objects.equals(pid, CustomerMenuConstant.MENU_ROOT_PID)) {
                route = CustomerMenuConstant.MENU_ROOT_ROUTE;
            } else {
                CustomerMenuEntity pm = getCustomerMenuById(pid);
                Preconditions.checkCondition(pm != null, "父菜单不存在");
                route = pm.getRoute() + "/" + pm.getId();
            }
            // 当前菜单的route发生改变时,子菜单的route也随着发生改变
            String oldLowerRoute = menu.getRoute() + "/" + menu.getId();
            String newLowerRoute = route + "/" + menu.getId();
            customerMenuDao.replaceRoute(oldLowerRoute, newLowerRoute);
        }
        BeanUtils.copyProperties(menuDto, menu);
        menu.setRoute(route);
        updateById(menu);

        CustomerMenuEvent event = new CustomerMenuEvent(this, Action.MODIFY,
                menu.getId(), route + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    @Transactional
    public void removeCustomerMenu(Long menuId) {
        CustomerMenuEntity menu = getById(menuId);
        removeById(menuId);
        String lowerRoute = menu.getRoute() + "/" + menuId;
        List<Long> lowerMenuIds = getCustomerLowerMenuIdsByRoute(lowerRoute);
        removeByIds(lowerMenuIds);
        CustomerMenuEvent event = new CustomerMenuEvent(this, Action.REMOVE,
                menuId, lowerRoute, lowerMenuIds);
        publisher.publishEvent(event);
    }

    @Override
    public List<Long> getCustomerLowerMenuIdsByRoute(String route) {
        List<CustomerMenuEntity> menus = list(new LambdaQueryWrapper<CustomerMenuEntity>()
                .select(CustomerMenuEntity::getId)
                .likeRight(CustomerMenuEntity::getRoute, route));
        return menus.stream().map(CustomerMenuEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getCustomerUpperMenuIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        List<CustomerMenuEntity> menus = list(new LambdaQueryWrapper<CustomerMenuEntity>()
                .select(CustomerMenuEntity::getRoute)
                .in(CustomerMenuEntity::getId, menuIds));
        return menus.stream()
                .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerMenuVo> getCustomerMenusByUserId(Long userId, boolean upper, boolean combine) {
        List<Long> roleIds = customerUserRoleService.getCustomerRoleIdsByUserId(userId);
        List<Long> menuIds = customerRoleMenuService.getCustomerMenuIdsByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        List<CustomerMenuEntity> menus = list(new LambdaQueryWrapper<CustomerMenuEntity>()
                .in(CustomerMenuEntity::getId, menuIds));
        if (upper) {
            List<Long> upperMenuIds = menus.stream()
                    .flatMap(m -> StrSplitter.splitPath(m.getRoute()).stream())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            List<CustomerMenuEntity> upperMenus = list(new LambdaQueryWrapper<CustomerMenuEntity>()
                    .in(CustomerMenuEntity::getId, upperMenuIds));
            menus.addAll(upperMenus);
        }
        List<CustomerMenuVo> mvs = menus.stream().map(m -> {
            CustomerMenuVo mv = new CustomerMenuVo();
            BeanUtils.copyProperties(m, mv);
            return mv;
        }).collect(Collectors.toList());
        if (combine && upper) {
            return CustomerMenuVo.combine(mvs);
        } else {
            return mvs;
        }
    }
}