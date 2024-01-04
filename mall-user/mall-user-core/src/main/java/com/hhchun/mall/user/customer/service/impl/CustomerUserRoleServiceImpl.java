package com.hhchun.mall.user.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.constant.CustomerOtherConstant;
import com.hhchun.mall.user.customer.dao.CustomerUserRoleDao;
import com.hhchun.mall.user.customer.entity.bo.CustomerRoleBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerUserRoleEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerUserRoleDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserRoleSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerRoleVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerRoleEvent;
import com.hhchun.mall.user.customer.event.CustomerUserRoleEvent;
import com.hhchun.mall.user.customer.service.CustomerUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("customerUserRoleService")
public class CustomerUserRoleServiceImpl extends ServiceImpl<CustomerUserRoleDao, CustomerUserRoleEntity> implements CustomerUserRoleService {
    @Autowired
    private CustomerUserRoleDao customerUserRoleDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void saveCustomerUserRoles(CustomerUserRoleDto userRoleDto) {
        Long userId = userRoleDto.getUserId();
        List<CustomerUserRoleDto.Role> roles = userRoleDto.getRoles();

        List<Long> unboundRoleIds = roles.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(CustomerUserRoleDto.Role::getRoleId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundRoleIds)) {
            remove(new LambdaQueryWrapper<CustomerUserRoleEntity>()
                    .eq(CustomerUserRoleEntity::getUserId, userId)
                    .in(CustomerUserRoleEntity::getRoleId, unboundRoleIds));
        }

        List<Long> boundRoleIds = roles.stream()
                .filter(p -> Objects.equals(p.getAction(), CustomerOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(CustomerUserRoleDto.Role::getRoleId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundRoleIds)) {
            List<CustomerUserRoleEntity> rolePermissions = boundRoleIds.stream().map(roleId -> {
                CustomerUserRoleEntity userRole = new CustomerUserRoleEntity();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }

        publisher.publishEvent(new CustomerUserRoleEvent(this, Action.SAVE, userId));
    }

    @Override
    public PageResult<CustomerRoleVo> getCustomerRoles(CustomerUserRoleSearchDto search) {
        IPage<CustomerRoleBo> page = customerUserRoleDao.getCustomerRoles(search.getPage(), search);
        List<CustomerRoleVo> rvs = page.getRecords().stream().map(rb -> {
            CustomerRoleVo rv = new CustomerRoleVo();
            BeanUtils.copyProperties(rb, rv);
            return rv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, rvs);
    }

    @Override
    public List<Long> getCustomerUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CustomerUserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CustomerUserRoleEntity::getUserId);
        wrapper.in(CustomerUserRoleEntity::getRoleId, roleIds);
        return list(wrapper).stream()
                .map(CustomerUserRoleEntity::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCustomerRemovedUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        return customerUserRoleDao.getCustomerRemovedUserIdsByRoleIds(roleIds);
    }

    @Override
    public List<Long> getCustomerRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<CustomerUserRoleEntity> userRoleWrapper = new LambdaQueryWrapper<CustomerUserRoleEntity>()
                .select(CustomerUserRoleEntity::getRoleId)
                .eq(CustomerUserRoleEntity::getUserId, userId);
        return list(userRoleWrapper)
                .stream()
                .map(CustomerUserRoleEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = CustomerRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedCustomerRoleEvent(CustomerRoleEvent event) {
        remove(new LambdaQueryWrapper<CustomerUserRoleEntity>()
                .eq(CustomerUserRoleEntity::getRoleId, event.getRoleId()));
    }


}