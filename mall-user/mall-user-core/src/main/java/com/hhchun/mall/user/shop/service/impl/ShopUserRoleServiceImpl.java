package com.hhchun.mall.user.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.constant.ShopOtherConstant;
import com.hhchun.mall.user.shop.dao.ShopUserRoleDao;
import com.hhchun.mall.user.shop.entity.bo.ShopRoleBo;
import com.hhchun.mall.user.shop.entity.domain.ShopUserRoleEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopUserRoleDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserRoleSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopRoleVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopRoleEvent;
import com.hhchun.mall.user.shop.event.ShopUserRoleEvent;
import com.hhchun.mall.user.shop.service.ShopUserRoleService;
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


@Service("shopUserRoleService")
public class ShopUserRoleServiceImpl extends ServiceImpl<ShopUserRoleDao, ShopUserRoleEntity> implements ShopUserRoleService {
    @Autowired
    private ShopUserRoleDao shopUserRoleDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public void saveShopUserRoles(ShopUserRoleDto userRoleDto) {
        Long userId = userRoleDto.getUserId();
        List<ShopUserRoleDto.Role> roles = userRoleDto.getRoles();

        List<Long> unboundRoleIds = roles.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(ShopUserRoleDto.Role::getRoleId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundRoleIds)) {
            remove(new LambdaQueryWrapper<ShopUserRoleEntity>()
                    .eq(ShopUserRoleEntity::getUserId, userId)
                    .in(ShopUserRoleEntity::getRoleId, unboundRoleIds));
        }

        List<Long> boundRoleIds = roles.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(ShopUserRoleDto.Role::getRoleId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundRoleIds)) {
            List<ShopUserRoleEntity> rolePermissions = boundRoleIds.stream().map(roleId -> {
                ShopUserRoleEntity userRole = new ShopUserRoleEntity();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }

        publisher.publishEvent(new ShopUserRoleEvent(this, Action.SAVE, userId));
    }

    @Override
    public PageResult<ShopRoleVo> getShopRoles(ShopUserRoleSearchDto search) {
        IPage<ShopRoleBo> page = shopUserRoleDao.getShopRoles(search.getPage(), search);
        List<ShopRoleVo> rvs = page.getRecords().stream().map(rb -> {
            ShopRoleVo rv = new ShopRoleVo();
            BeanUtils.copyProperties(rb, rv);
            return rv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, rvs);
    }

    @Override
    public List<Long> getShopUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ShopUserRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ShopUserRoleEntity::getUserId);
        wrapper.in(ShopUserRoleEntity::getRoleId, roleIds);
        return list(wrapper).stream()
                .map(ShopUserRoleEntity::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getShopRemovedUserIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        return shopUserRoleDao.getShopRemovedUserIdsByRoleIds(roleIds);
    }

    @Override
    public List<Long> getShopRoleIdsByUserId(Long userId) {
        LambdaQueryWrapper<ShopUserRoleEntity> userRoleWrapper = new LambdaQueryWrapper<ShopUserRoleEntity>()
                .select(ShopUserRoleEntity::getRoleId)
                .eq(ShopUserRoleEntity::getUserId, userId);
        return list(userRoleWrapper)
                .stream()
                .map(ShopUserRoleEntity::getRoleId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopRoleEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopRoleEvent(ShopRoleEvent event) {
        remove(new LambdaQueryWrapper<ShopUserRoleEntity>()
                .eq(ShopUserRoleEntity::getRoleId, event.getRoleId()));
    }


}