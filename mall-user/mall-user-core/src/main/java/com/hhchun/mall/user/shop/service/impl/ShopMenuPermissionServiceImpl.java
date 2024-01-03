package com.hhchun.mall.user.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.constant.ShopOtherConstant;
import com.hhchun.mall.user.shop.dao.ShopMenuPermissionDao;
import com.hhchun.mall.user.shop.entity.bo.ShopPermissionBo;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuEntity;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuPermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopMenuPermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopMenuPermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopMenuEvent;
import com.hhchun.mall.user.shop.event.ShopMenuPermissionEvent;
import com.hhchun.mall.user.shop.event.ShopPermissionEvent;
import com.hhchun.mall.user.shop.service.ShopMenuPermissionService;
import com.hhchun.mall.user.shop.service.ShopMenuService;
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


@Service("shopMenuPermissionService")
public class ShopMenuPermissionServiceImpl extends ServiceImpl<ShopMenuPermissionDao, ShopMenuPermissionEntity> implements ShopMenuPermissionService {

    @Autowired
    private ShopMenuService shopMenuService;
    @Autowired
    private ShopMenuPermissionDao shopMenuPermissionDao;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void saveMenuPermissions(ShopMenuPermissionDto menuPermissionDto) {
        Long menuId = menuPermissionDto.getMenuId();
        ShopMenuEntity menu = shopMenuService.getShopMenuById(menuId);
        List<ShopMenuPermissionDto.Permission> permissions = menuPermissionDto.getPermissions();
        Preconditions.checkCondition(menu != null, "菜单不存在");

        List<Long> unboundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_UNBOUND))
                .map(ShopMenuPermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unboundPermissionIds)) {
            remove(new LambdaQueryWrapper<ShopMenuPermissionEntity>()
                    .eq(ShopMenuPermissionEntity::getMenuId, menuId)
                    .in(ShopMenuPermissionEntity::getPermissionId, unboundPermissionIds));
        }

        List<Long> boundPermissionIds = permissions.stream()
                .filter(p -> Objects.equals(p.getAction(), ShopOtherConstant.TABLE_RELATION_ACTION_BOUND))
                .map(ShopMenuPermissionDto.Permission::getPermissionId)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(boundPermissionIds)) {
            List<ShopMenuPermissionEntity> rolePermissions = boundPermissionIds.stream().map(permissionId -> {
                ShopMenuPermissionEntity menuPermission = new ShopMenuPermissionEntity();
                menuPermission.setMenuId(menuId);
                menuPermission.setPermissionId(permissionId);
                return menuPermission;
            }).collect(Collectors.toList());
            saveBatch(rolePermissions);
        }
        ShopMenuPermissionEvent event = new ShopMenuPermissionEvent(this, Action.SAVE,
                menuId, menu.getRoute() + "/" + menu.getId());
        publisher.publishEvent(event);
    }

    @Override
    public PageResult<ShopPermissionVo> getShopPermissions(ShopMenuPermissionSearchDto search) {
        IPage<ShopPermissionBo> page = shopMenuPermissionDao.getShopPermissions(search.getPage(), search);
        List<ShopPermissionVo> pvs = page.getRecords().stream().map(pb -> {
            ShopPermissionVo pv = new ShopPermissionVo();
            BeanUtils.copyProperties(pb, pv);
            return pv;
        }).collect(Collectors.toList());
        return PageResult.convert(page, pvs);
    }

    @Override
    public List<Long> getShopPermissionIdsByMenuIds(Collection<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ShopMenuPermissionEntity> wrapper = new LambdaQueryWrapper<ShopMenuPermissionEntity>()
                .select(ShopMenuPermissionEntity::getPermissionId)
                .in(ShopMenuPermissionEntity::getMenuId, menuIds);
        return list(wrapper)
                .stream().map(ShopMenuPermissionEntity::getPermissionId)
                .collect(Collectors.toList());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopPermissionEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopPermissionEvent(ShopPermissionEvent event) {
        remove(new LambdaQueryWrapper<ShopMenuPermissionEntity>()
                .eq(ShopMenuPermissionEntity::getPermissionId, event.getPermissionId()));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(value = ShopMenuEvent.class, condition = "event.action.equals(event.action.REMOVE)")
    public void listenRemovedShopMenuEvent(ShopMenuEvent event) {
        List<Long> menuIds = Lists.newArrayList();
        menuIds.add(event.getMenuId());
        menuIds.addAll(event.getLowerMenuIds());
        remove(new LambdaQueryWrapper<ShopMenuPermissionEntity>()
                .in(ShopMenuPermissionEntity::getMenuId, menuIds));
    }
}