package com.hhchun.mall.user.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.dao.ShopRoleDao;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopRoleDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopRoleVo;
import com.hhchun.mall.user.shop.event.Action;
import com.hhchun.mall.user.shop.event.ShopRoleEvent;
import com.hhchun.mall.user.shop.service.ShopRoleService;
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


@Service("shopRoleService")
public class ShopRoleServiceImpl extends ServiceImpl<ShopRoleDao, ShopRoleEntity> implements ShopRoleService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void saveShopRole(ShopRoleDto roleDto) {
        String symbol = roleDto.getSymbol();
        ShopRoleEntity role = getShopRoleBySymbol(symbol);
        Preconditions.checkCondition(role == null, "角色标识已存在");
        role = new ShopRoleEntity();
        BeanUtils.copyProperties(roleDto, role);
        save(role);
    }

    @Override
    @Nullable
    public ShopRoleEntity getShopRoleBySymbol(@NotNull String symbol) {
        return getOne(new LambdaQueryWrapper<ShopRoleEntity>().eq(ShopRoleEntity::getSymbol, symbol));
    }

    @Override
    public void modifyShopRole(ShopRoleDto roleDto) {
        Long roleId = roleDto.getId();
        String symbol = roleDto.getSymbol();
        ShopRoleEntity role = getShopRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");
        if (StringUtils.hasLength(symbol)) {
            ShopRoleEntity symbolRole = getShopRoleBySymbol(symbol);
            Preconditions.checkCondition(symbolRole == null
                            || Objects.equals(symbolRole.getId(), roleId),
                    "角色标识已存在");
        }
        ShopRoleEntity modifyRole = new ShopRoleEntity();
        BeanUtils.copyProperties(roleDto, modifyRole);
        updateById(modifyRole);

        publisher.publishEvent(new ShopRoleEvent(this, Action.MODIFY, roleId));
    }

    @Override
    @Nullable
    public ShopRoleEntity getShopRoleById(@NotNull Long id) {
        return getById(id);
    }

    @Override
    public void removeShopRole(Long roleId) {
        removeById(roleId);

        publisher.publishEvent(new ShopRoleEvent(this, Action.REMOVE, roleId));
    }

    @Override
    public PageResult<ShopRoleVo> getShopRoleList(ShopRoleSearchDto search) {
        Long id = search.getId();
        String symbol = search.getSymbol();
        String name = search.getName();
        LambdaQueryWrapper<ShopRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, ShopRoleEntity::getId, id);
        wrapper.eq(StringUtils.hasLength(symbol), ShopRoleEntity::getSymbol, symbol);
        wrapper.like(StringUtils.hasLength(name), ShopRoleEntity::getName, name);
        IPage<ShopRoleEntity> page = page(search.getPage(), wrapper);
        List<ShopRoleEntity> roles = page.getRecords();
        List<ShopRoleVo> roleVos = roles.stream().map(role -> {
            ShopRoleVo roleVo = new ShopRoleVo();
            BeanUtils.copyProperties(role, roleVo);
            return roleVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, roleVos);
    }
}