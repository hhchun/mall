package com.hhchun.mall.user.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.constant.CustomerOtherConstant;
import com.hhchun.mall.user.customer.dao.CustomerMenuPermissionDao;
import com.hhchun.mall.user.customer.entity.bo.CustomerPermissionBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuEntity;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuPermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerMenuPermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerMenuPermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerMenuEvent;
import com.hhchun.mall.user.customer.event.CustomerMenuPermissionEvent;
import com.hhchun.mall.user.customer.event.CustomerPermissionEvent;
import com.hhchun.mall.user.customer.service.CustomerMenuPermissionService;
import com.hhchun.mall.user.customer.service.CustomerMenuService;
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


@Service("customerMenuPermissionService")
public class CustomerMenuPermissionServiceImpl extends ServiceImpl<CustomerMenuPermissionDao, CustomerMenuPermissionEntity> implements CustomerMenuPermissionService {

    @Autowired
    private CustomerMenuService customerMenuService;
    @Autowired
    private CustomerMenuPermissionDao customerMenuPermissionDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveMenuPermissions(CustomerMenuPermissionDto menuPermissionDto) {
        Long menuId = menuPermissionDto.getMenuId();
        CustomerMenuEntity menu = customerMenuService.getCustomerMenuById(menuId);
        List<CustomerMenuPermissionDto.Permission> permissions = menuPermissionDto.getPermissions();
        Preconditions.checkCondition(menu != null, "菜单不存在");

        List<Long> unboundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(CustomerMenuPermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundPermissionIds)) {
            remove(new LambdaQueryWrapper<CustomerMenuPermissionEntity>()
                    .eq(CustomerMenuPermissionEntity::getMenuId, menuId)
                    .in(CustomerMenuPermissionEntity::getPermissionId, unboundPermissionIds));
        }

        List<Long> boundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(CustomerMenuPermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundPermissionIds)) {
            List<CustomerMenuPermissionEntity> rolePermissions = boundPermissionIds.stream().map(permissionId -> {
                CustomerMenuPermissionEntity menuPermission = new CustomerMenuPermissionEntity();
                menuPermission.setMenuId(menuId);
                menuPermission.setPermissionId(permissionId);
                return menuPermission;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }
        CustomerMenuPermissionEvent event = new CustomerMenuPermissionEvent(this, Action.SAVE,
                menuId, menu.getRoute() + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    public PageResult<CustomerPermissionVo> getCustomerPermissions(CustomerMenuPermissionSearchDto search) {
        IPage<CustomerPermissionBo> page = customerMenuPermissionDao.getCustomerPermissions(search.getPage(), search);
        List<CustomerPermissionVo> pvs = page.getRecords().stream().map(pb -> {
            CustomerPermissionVo pv = new CustomerPermissionVo();
            BeanUtils.copyProperties(pb, pv);
            return pv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, pvs);
    }

    @Override
    public List<Long> getCustomerPermissionIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CustomerMenuPermissionEntity> wrapper = new LambdaQueryWrapper<CustomerMenuPermissionEntity>()
                .select(CustomerMenuPermissionEntity::getPermissionId)
                .in(CustomerMenuPermissionEntity::getMenuId, menuIds);
        return list(wrapper)
                .stream().map(CustomerMenuPermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerPermissionEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerPermissionEvent(CustomerPermissionEvent event) {
        remove(new LambdaQueryWrapper<CustomerMenuPermissionEntity>()
                .eq(CustomerMenuPermissionEntity::getPermissionId, event.getPermissionId()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerMenuEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerMenuEvent(CustomerMenuEvent event) {
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(event.getMenuId());
        menuIds.addAll(event.getLowerMenuIds());
        remove(new LambdaQueryWrapper<CustomerMenuPermissionEntity>()
                .in(CustomerMenuPermissionEntity::getMenuId, menuIds));
    }
}