package com.hhchun.mall.user.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.dao.CustomerPermissionDao;
import com.hhchun.mall.user.customer.entity.domain.CustomerPermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerPermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerPermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.hhchun.mall.user.customer.event.Action;
import com.hhchun.mall.user.customer.event.CustomerPermissionEvent;
import com.hhchun.mall.user.customer.service.CustomerPermissionService;
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


@Service("customerPermissionService")
public class CustomerPermissionServiceImpl extends ServiceImpl<CustomerPermissionDao, CustomerPermissionEntity> implements CustomerPermissionService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void saveCustomerPermission(CustomerPermissionDto permissionDto) {
        String symbol = permissionDto.getSymbol();
        CustomerPermissionEntity permission = getCustomerPermissionBySymbol(symbol);
        Preconditions.checkCondition(permission == null, "权限标识已存在");
        permission = new CustomerPermissionEntity();
        BeanUtils.copyProperties(permissionDto, permission);
        save(permission);
        publisher.publishEvent(new CustomerPermissionEvent(this, Action.SAVE, permission.getId()));
    }

    @Override
    public void modifyCustomerPermission(CustomerPermissionDto permissionDto) {
        Long permissionId = permissionDto.getId();
        CustomerPermissionEntity permission = getCustomerPermissionById(permissionId);
        Preconditions.checkCondition(permission != null, "修改的权限不存在");
        String symbol = permissionDto.getSymbol();
        if (StringUtils.hasLength(symbol)) {
            CustomerPermissionEntity symbolPermission = getCustomerPermissionBySymbol(symbol);
            Preconditions.checkCondition(symbolPermission == null
                            || Objects.equals(symbolPermission.getId(), permissionId),
                    "权限标识已存在");
        }
        CustomerPermissionEntity modifyPermission = new CustomerPermissionEntity();
        BeanUtils.copyProperties(permissionDto, modifyPermission);
        updateById(modifyPermission);
        publisher.publishEvent(new CustomerPermissionEvent(this, Action.MODIFY, permissionId));
    }

    @Nullable
    @Override
    public CustomerPermissionEntity getCustomerPermissionBySymbol(@NotNull String symbol) {
        Preconditions.checkArgument(StringUtils.hasLength(symbol), "symbol is empty!");
        return getOne(new LambdaQueryWrapper<CustomerPermissionEntity>()
                .eq(CustomerPermissionEntity::getSymbol, symbol));
    }

    @Nullable
    @Override
    public CustomerPermissionEntity getCustomerPermissionById(@NotNull Long id) {
        Preconditions.checkArgument(id != null, "id is null!");
        return getById(id);
    }

    @Override
    public void removeCustomerPermission(Long permissionId) {
        removeById(permissionId);
        publisher.publishEvent(new CustomerPermissionEvent(this, Action.REMOVE, permissionId));
    }

    @Override
    public PageResult<CustomerPermissionVo> getCustomerPermissionList(CustomerPermissionSearchDto search) {
        Long id = search.getId();
        String symbol = search.getSymbol();
        Integer overt = search.getOvert();
        String name = search.getName();
        String subject = search.getSubject();

        LambdaQueryWrapper<CustomerPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, CustomerPermissionEntity::getId, id);
        wrapper.eq(StringUtils.hasLength(symbol), CustomerPermissionEntity::getSymbol, symbol);
        wrapper.like(StringUtils.hasLength(name), CustomerPermissionEntity::getName, name);
        wrapper.like(StringUtils.hasLength(subject), CustomerPermissionEntity::getSubject, subject);
        wrapper.eq(overt != null, CustomerPermissionEntity::getOvert, overt);
        IPage<CustomerPermissionEntity> page = page(search.getPage(), wrapper);
        List<CustomerPermissionEntity> permissions = page.getRecords();
        List<CustomerPermissionVo> permissionVos = permissions.stream().map(permission -> {
            CustomerPermissionVo permissionVo = new CustomerPermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, permissionVos);
    }
}