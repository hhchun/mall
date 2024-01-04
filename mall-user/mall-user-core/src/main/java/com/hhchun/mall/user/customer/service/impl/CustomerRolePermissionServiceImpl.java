package com.hhchun.mall.user.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.constant.CustomerOtherConstant;
import com.hhchun.mall.user.customer.dao.CustomerRolePermissionDao;
import com.hhchun.mall.user.customer.entity.bo.CustomerPermissionBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleEntity;
import com.hhchun.mall.user.customer.entity.domain.CustomerRolePermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerRolePermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRolePermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerPermissionEvent;
import com.hhchun.mall.user.customer.event.CustomerRoleEvent;
import com.hhchun.mall.user.customer.event.CustomerRolePermissionEvent;
import com.hhchun.mall.user.customer.service.CustomerRolePermissionService;
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

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("customerRolePermissionService")
public class CustomerRolePermissionServiceImpl extends ServiceImpl<CustomerRolePermissionDao, CustomerRolePermissionEntity> implements CustomerRolePermissionService {

    @Autowired
    private CustomerRoleService customerRoleService;
    @Autowired
    private CustomerRolePermissionDao customerRolePermissionDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveCustomerRolePermissions(CustomerRolePermissionDto rolePermissionDto) {
        Long roleId = rolePermissionDto.getRoleId();
        List<CustomerRolePermissionDto.Permission> permissions = rolePermissionDto.getPermissions();
        CustomerRoleEntity role = customerRoleService.getCustomerRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<Long> unboundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(CustomerRolePermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundPermissionIds)) {
            remove(new LambdaQueryWrapper<CustomerRolePermissionEntity>()
                    .eq(CustomerRolePermissionEntity::getRoleId, roleId)
                    .in(CustomerRolePermissionEntity::getPermissionId, unboundPermissionIds));
        }

        List<Long> boundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(CustomerRolePermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundPermissionIds)) {
            List<CustomerRolePermissionEntity> rolePermissions = boundPermissionIds.stream().map(permissionId -> {
                CustomerRolePermissionEntity rolePermission = new CustomerRolePermissionEntity();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }

        publisher.publishEvent(new CustomerRolePermissionEvent(this, Action.SAVE, roleId));
    }

    @Override
    public PageResult<CustomerPermissionVo> getCustomerPermissions(CustomerRolePermissionSearchDto search) {
        IPage<CustomerPermissionBo> page = customerRolePermissionDao.getCustomerPermissions(search.getPage(), search);
        List<CustomerPermissionVo> pvs = page.getRecords().stream().map(pb -> {
            CustomerPermissionVo pv = new CustomerPermissionVo();
            BeanUtils.copyProperties(pb, pv);
            return pv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, pvs);
    }

    @Override
    public List<Long> getCustomerRoleIdsByPermissionId(Long permissionId) {
        LambdaQueryWrapper<CustomerRolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CustomerRolePermissionEntity::getRoleId);
        wrapper.eq(CustomerRolePermissionEntity::getPermissionId, permissionId);
        return list(wrapper).stream()
                .map(CustomerRolePermissionEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<Long> getCustomerRemovedRoleIdsByPermissionId(Long permissionId) {
        return customerRolePermissionDao.getCustomerRemovedRoleIdsByPermissionId(permissionId);
    }

    @Nonnull
    @Override
    public List<Long> getCustomerPermissionIdsByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CustomerRolePermissionEntity> wrapper = new LambdaQueryWrapper<CustomerRolePermissionEntity>()
                .select(CustomerRolePermissionEntity::getPermissionId)
                .in(CustomerRolePermissionEntity::getRoleId, roleIds);
        return list(wrapper)
                .stream()
                .map(CustomerRolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerRoleEvent(CustomerRoleEvent event) {
        remove(new LambdaQueryWrapper<CustomerRolePermissionEntity>()
                .eq(CustomerRolePermissionEntity::getRoleId, event.getRoleId()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerPermissionEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerPermissionEvent(CustomerPermissionEvent event) {
        remove(new LambdaQueryWrapper<CustomerRolePermissionEntity>()
                .eq(CustomerRolePermissionEntity::getPermissionId, event.getPermissionId()));
    }
}