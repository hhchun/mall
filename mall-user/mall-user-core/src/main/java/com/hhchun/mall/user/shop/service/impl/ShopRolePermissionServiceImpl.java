package com.hhchun.mall.user.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.constant.ShopOtherConstant;
import com.hhchun.mall.user.shop.dao.ShopRolePermissionDao;
import com.hhchun.mall.user.shop.entity.bo.ShopPermissionBo;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleEntity;
import com.hhchun.mall.user.shop.entity.domain.ShopRolePermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopRolePermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRolePermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopPermissionEvent;
import com.hhchun.mall.user.shop.event.ShopRoleEvent;
import com.hhchun.mall.user.shop.event.ShopRolePermissionEvent;
import com.hhchun.mall.user.shop.service.ShopRolePermissionService;
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

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("shopRolePermissionService")
public class ShopRolePermissionServiceImpl extends ServiceImpl<ShopRolePermissionDao, ShopRolePermissionEntity> implements ShopRolePermissionService {

    @Autowired
    private ShopRoleService shopRoleService;
    @Autowired
    private ShopRolePermissionDao shopRolePermissionDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveShopRolePermissions(ShopRolePermissionDto rolePermissionDto) {
        Long roleId = rolePermissionDto.getRoleId();
        List<ShopRolePermissionDto.Permission> permissions = rolePermissionDto.getPermissions();
        ShopRoleEntity role = shopRoleService.getShopRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");

        List<Long> unboundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(ShopRolePermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundPermissionIds)) {
            remove(new LambdaQueryWrapper<ShopRolePermissionEntity>()
                    .eq(ShopRolePermissionEntity::getRoleId, roleId)
                    .in(ShopRolePermissionEntity::getPermissionId, unboundPermissionIds));
        }

        List<Long> boundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(ShopRolePermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundPermissionIds)) {
            List<ShopRolePermissionEntity> rolePermissions = boundPermissionIds.stream().map(permissionId -> {
                ShopRolePermissionEntity rolePermission = new ShopRolePermissionEntity();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }

        publisher.publishEvent(new ShopRolePermissionEvent(this, Action.SAVE, roleId));
    }

    @Override
    public PageResult<ShopPermissionVo> getShopPermissions(ShopRolePermissionSearchDto search) {
        IPage<ShopPermissionBo> page = shopRolePermissionDao.getShopPermissions(search.getPage(), search);
        List<ShopPermissionVo> pvs = page.getRecords().stream().map(pb -> {
            ShopPermissionVo pv = new ShopPermissionVo();
            BeanUtils.copyProperties(pb, pv);
            return pv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, pvs);
    }

    @Override
    public List<Long> getShopRoleIdsByPermissionId(Long permissionId) {
        LambdaQueryWrapper<ShopRolePermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ShopRolePermissionEntity::getRoleId);
        wrapper.eq(ShopRolePermissionEntity::getPermissionId, permissionId);
        return list(wrapper).stream()
                .map(ShopRolePermissionEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<Long> getShopRemovedRoleIdsByPermissionId(Long permissionId) {
        return shopRolePermissionDao.getShopRemovedRoleIdsByPermissionId(permissionId);
    }

    @Nonnull
    @Override
    public List<Long> getShopPermissionIdsByRoleIds(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ShopRolePermissionEntity> wrapper = new LambdaQueryWrapper<ShopRolePermissionEntity>()
                .select(ShopRolePermissionEntity::getPermissionId)
                .in(ShopRolePermissionEntity::getRoleId, roleIds);
        return list(wrapper)
                .stream()
                .map(ShopRolePermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopRoleEvent(ShopRoleEvent event) {
        remove(new LambdaQueryWrapper<ShopRolePermissionEntity>()
                .eq(ShopRolePermissionEntity::getRoleId, event.getRoleId()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopPermissionEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopPermissionEvent(ShopPermissionEvent event) {
        remove(new LambdaQueryWrapper<ShopRolePermissionEntity>()
                .eq(ShopRolePermissionEntity::getPermissionId, event.getPermissionId()));
    }
}