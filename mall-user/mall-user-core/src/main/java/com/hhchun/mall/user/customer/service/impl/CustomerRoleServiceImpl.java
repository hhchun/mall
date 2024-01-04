package com.hhchun.mall.user.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.dao.CustomerRoleDao;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerRoleDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerRoleVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerRoleEvent;
import com.hhchun.mall.user.customer.service.CustomerRoleService;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("customerRoleService")
public class CustomerRoleServiceImpl extends ServiceImpl<CustomerRoleDao, CustomerRoleEntity> implements CustomerRoleService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void saveCustomerRole(CustomerRoleDto roleDto) {
        String symbol = roleDto.getSymbol();
        CustomerRoleEntity role = getCustomerRoleBySymbol(symbol);
        Preconditions.checkCondition(role == null, "角色标识已存在");
        role = new CustomerRoleEntity();
        BeanUtils.copyProperties(roleDto, role);
        save(role);
    }

    @Override
    @Nullable
    public CustomerRoleEntity getCustomerRoleBySymbol(@NotNull String symbol) {
        return getOne(new LambdaQueryWrapper<CustomerRoleEntity>().eq(CustomerRoleEntity::getSymbol, symbol));
    }

    @Override
    public void modifyCustomerRole(CustomerRoleDto roleDto) {
        Long roleId = roleDto.getId();
        String symbol = roleDto.getSymbol();
        CustomerRoleEntity role = getCustomerRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");
        if (StringUtils.hasLength(symbol)) {
            CustomerRoleEntity symbolRole = getCustomerRoleBySymbol(symbol);
            Preconditions.checkCondition(symbolRole == null
                            || Objects.equals(symbolRole.getId(), roleId),
                    "角色标识已存在");
        }
        CustomerRoleEntity modifyRole = new CustomerRoleEntity();
        BeanUtils.copyProperties(roleDto, modifyRole);
        updateById(modifyRole);

        publisher.publishEvent(new CustomerRoleEvent(this, Action.MODIFY, roleId));
    }

    @Override
    @Nullable
    public CustomerRoleEntity getCustomerRoleById(@NotNull Long id) {
        return getById(id);
    }

    @Override
    public void removeCustomerRole(Long roleId) {
        removeById(roleId);

        publisher.publishEvent(new CustomerRoleEvent(this, Action.REMOVE, roleId));
    }

    @Override
    public PageResult<CustomerRoleVo> getCustomerRoleList(CustomerRoleSearchDto search) {
        Long id = search.getId();
        String symbol = search.getSymbol();
        String name = search.getName();
        LambdaQueryWrapper<CustomerRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, CustomerRoleEntity::getId, id);
        wrapper.eq(StringUtils.hasLength(symbol), CustomerRoleEntity::getSymbol, symbol);
        wrapper.like(StringUtils.hasLength(name), CustomerRoleEntity::getName, name);
        IPage<CustomerRoleEntity> page = page(search.getPage(), wrapper);
        List<CustomerRoleEntity> roles = page.getRecords();
        List<CustomerRoleVo> roleVos = roles.stream().map(role -> {
            CustomerRoleVo roleVo = new CustomerRoleVo();
            BeanUtils.copyProperties(role, roleVo);
            return roleVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, roleVos);
    }
}