package com.hhchun.mall.user.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.dao.ShopPermissionDao;
import com.hhchun.mall.user.shop.entity.domain.ShopPermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopPermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopPermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopPermissionEvent;
import com.hhchun.mall.user.shop.service.ShopPermissionService;
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


@Service("shopPermissionService")
public class ShopPermissionServiceImpl extends ServiceImpl<ShopPermissionDao, ShopPermissionEntity> implements ShopPermissionService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void saveShopPermission(ShopPermissionDto permissionDto) {
        String symbol = permissionDto.getSymbol();
        ShopPermissionEntity permission = getShopPermissionBySymbol(symbol);
        Preconditions.checkCondition(permission == null, "权限标识已存在");
        permission = new ShopPermissionEntity();
        BeanUtils.copyProperties(permissionDto, permission);
        save(permission);
        publisher.publishEvent(new ShopPermissionEvent(this, Action.SAVE, permission.getId()));
    }

    @Override
    public void modifyShopPermission(ShopPermissionDto permissionDto) {
        Long permissionId = permissionDto.getId();
        ShopPermissionEntity permission = getShopPermissionById(permissionId);
        Preconditions.checkCondition(permission != null, "修改的权限不存在");
        String symbol = permissionDto.getSymbol();
        if (StringUtils.hasLength(symbol)) {
            ShopPermissionEntity symbolPermission = getShopPermissionBySymbol(symbol);
            Preconditions.checkCondition(symbolPermission == null
                            || Objects.equals(symbolPermission.getId(), permissionId),
                    "权限标识已存在");
        }
        ShopPermissionEntity modifyPermission = new ShopPermissionEntity();
        BeanUtils.copyProperties(permissionDto, modifyPermission);
        updateById(modifyPermission);
        publisher.publishEvent(new ShopPermissionEvent(this, Action.MODIFY, permissionId));
    }

    @Nullable
    @Override
    public ShopPermissionEntity getShopPermissionBySymbol(@NotNull String symbol) {
        Preconditions.checkArgument(StringUtils.hasLength(symbol), "symbol is empty!");
        return getOne(new LambdaQueryWrapper<ShopPermissionEntity>()
                .eq(ShopPermissionEntity::getSymbol, symbol));
    }

    @Nullable
    @Override
    public ShopPermissionEntity getShopPermissionById(@NotNull Long id) {
        Preconditions.checkArgument(id != null, "id is null!");
        return getById(id);
    }

    @Override
    public void removeShopPermission(Long permissionId) {
        removeById(permissionId);
        publisher.publishEvent(new ShopPermissionEvent(this, Action.REMOVE, permissionId));
    }

    @Override
    public PageResult<ShopPermissionVo> getShopPermissionList(ShopPermissionSearchDto search) {
        Long id = search.getId();
        String symbol = search.getSymbol();
        Integer overt = search.getOvert();
        String name = search.getName();
        String subject = search.getSubject();

        LambdaQueryWrapper<ShopPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, ShopPermissionEntity::getId, id);
        wrapper.eq(StringUtils.hasLength(symbol), ShopPermissionEntity::getSymbol, symbol);
        wrapper.like(StringUtils.hasLength(name), ShopPermissionEntity::getName, name);
        wrapper.like(StringUtils.hasLength(subject), ShopPermissionEntity::getSubject, subject);
        wrapper.eq(overt != null, ShopPermissionEntity::getOvert, overt);
        IPage<ShopPermissionEntity> page = page(search.getPage(), wrapper);
        List<ShopPermissionEntity> permissions = page.getRecords();
        List<ShopPermissionVo> permissionVos = permissions.stream().map(permission -> {
            ShopPermissionVo permissionVo = new ShopPermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, permissionVos);
    }
}