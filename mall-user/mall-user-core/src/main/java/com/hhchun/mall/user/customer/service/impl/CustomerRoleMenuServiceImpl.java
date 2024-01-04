package com.hhchun.mall.user.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.user.customer.constant.CustomerOtherConstant;
import com.hhchun.mall.user.customer.dao.CustomerRoleMenuDao;
import com.hhchun.mall.user.customer.entity.bo.CustomerMenuBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleEntity;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleMenuEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerRoleMenuDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleMenuSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerMenuVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerMenuEvent;
import com.hhchun.mall.user.customer.event.CustomerRoleEvent;
import com.hhchun.mall.user.customer.event.CustomerRoleMenuEvent;
import com.hhchun.mall.user.customer.service.CustomerRoleMenuService;
import com.hhchun.mall.user.customer.service.CustomerRoleService;
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


@Service("customerRoleMenuService")
public class CustomerRoleMenuServiceImpl extends ServiceImpl<CustomerRoleMenuDao, CustomerRoleMenuEntity> implements CustomerRoleMenuService {

    @Autowired
    private CustomerRoleService customerRoleService;
    @Autowired
    private CustomerRoleMenuDao customerRoleMenuDao;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveCustomerRoleMenus(CustomerRoleMenuDto roleMenuDto) {
        Long roleId = roleMenuDto.getRoleId();
        List<CustomerRoleMenuDto.Menu> menus = roleMenuDto.getMenus();
        CustomerRoleEntity role = customerRoleService.getCustomerRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<Long> unboundMenuIds = menus.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(CustomerRoleMenuDto.Menu::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundMenuIds)) {
            remove(new LambdaQueryWrapper<CustomerRoleMenuEntity>()
                    .eq(CustomerRoleMenuEntity::getRoleId, roleId)
                    .in(CustomerRoleMenuEntity::getMenuId, unboundMenuIds));
        }

        List<Long> boundMenuIds = menus.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(CustomerRoleMenuDto.Menu::getMenuId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundMenuIds)) {
            List<CustomerRoleMenuEntity> roleMenus = boundMenuIds.stream().map(menuId -> {
                CustomerRoleMenuEntity roleMenu = new CustomerRoleMenuEntity();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            saveBatch(roleMenus);
        }

        publisher.publishEvent(new CustomerRoleMenuEvent(this, Action.SAVE, roleId));

    }

    @Override
    public List<CustomerMenuVo> getCustomerMenuTree(CustomerRoleMenuSearchDto search) {
        List<CustomerMenuBo> mbs = customerRoleMenuDao.getCustomerMenuTree(search);
        return mbs.stream().map(mb -> {
            CustomerMenuVo mv = new CustomerMenuVo();
            BeanUtils.copyProperties(mb, mv);
            return mv;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getCustomerRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CustomerRoleMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CustomerRoleMenuEntity::getRoleId);
        wrapper.in(CustomerRoleMenuEntity::getMenuId, menuIds);
        return list(wrapper).stream()
                .map(CustomerRoleMenuEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCustomerRemovedRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        return customerRoleMenuDao.getCustomerRemovedRoleIdsByMenuIds(menuIds);
    }

    @Override
    public List<Long> getCustomerMenuIdsByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CustomerRoleMenuEntity> wrapper = new LambdaQueryWrapper<CustomerRoleMenuEntity>()
                .select(CustomerRoleMenuEntity::getMenuId)
                .in(CustomerRoleMenuEntity::getRoleId, roleIds);
        return list(wrapper)
                .stream().map(CustomerRoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerRoleEvent(CustomerRoleEvent event) {
        remove(new LambdaQueryWrapper<CustomerRoleMenuEntity>()
                .eq(CustomerRoleMenuEntity::getRoleId, event.getRoleId()));
    }

    @Transactional
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerMenuEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerMenuEvent(CustomerMenuEvent event) {
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(event.getMenuId());
        menuIds.addAll(event.getLowerMenuIds());
        remove(new LambdaQueryWrapper<CustomerRoleMenuEntity>()
                .in(CustomerRoleMenuEntity::getMenuId, menuIds));
    }
}