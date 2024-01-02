package com.hhchun.mall.user.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.dao.PlatformRoleDao;
import com.hhchun.mall.user.platform.entity.dto.PlatformRoleDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRoleSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformRoleVo;
import com.hhchun.mall.user.platform.event.Action;
import com.hhchun.mall.user.platform.event.PlatformRoleEvent;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.hhchun.mall.user.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.user.platform.service.PlatformRoleService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("platformRoleService")
public class PlatformRoleServiceImpl extends ServiceImpl<PlatformRoleDao, PlatformRoleEntity> implements PlatformRoleService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void savePlatformRole(PlatformRoleDto roleDto) {
        String symbol = roleDto.getSymbol();
        PlatformRoleEntity role = getPlatformRoleBySymbol(symbol);
        Preconditions.checkCondition(role == null, "角色标识已存在");
        role = new PlatformRoleEntity();
        BeanUtils.copyProperties(roleDto, role);
        save(role);
    }

    @Override
    @Nullable
    public PlatformRoleEntity getPlatformRoleBySymbol(@NotNull String symbol) {
        return getOne(new LambdaQueryWrapper<PlatformRoleEntity>().eq(PlatformRoleEntity::getSymbol, symbol));
    }

    @Override
    public void modifyPlatformRole(PlatformRoleDto roleDto) {
        Long roleId = roleDto.getId();
        String symbol = roleDto.getSymbol();
        PlatformRoleEntity role = getPlatformRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");
        if (StringUtils.hasLength(symbol)) {
            PlatformRoleEntity symbolRole = getPlatformRoleBySymbol(symbol);
            Preconditions.checkCondition(symbolRole == null
                            || Objects.equals(symbolRole.getId(), roleId),
                    "角色标识已存在");
        }
        PlatformRoleEntity modifyRole = new PlatformRoleEntity();
        BeanUtils.copyProperties(roleDto, modifyRole);
        updateById(modifyRole);

        publisher.publishEvent(new PlatformRoleEvent(this, Action.MODIFY, roleId));
    }

    @Override
    @Nullable
    public PlatformRoleEntity getPlatformRoleById(@NotNull Long id) {
        return getById(id);
    }

    @Override
    public void removePlatformRole(Long roleId) {
        removeById(roleId);

        publisher.publishEvent(new PlatformRoleEvent(this, Action.REMOVE, roleId));
    }

    @Override
    public PageResult<PlatformRoleVo> getPlatformRoleList(PlatformRoleSearchDto search) {
        Long id = search.getId();
        String symbol = search.getSymbol();
        String name = search.getName();
        LambdaQueryWrapper<PlatformRoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, PlatformRoleEntity::getId, id);
        wrapper.eq(StringUtils.hasLength(symbol), PlatformRoleEntity::getSymbol, symbol);
        wrapper.like(StringUtils.hasLength(name), PlatformRoleEntity::getName, name);
        IPage<PlatformRoleEntity> page = page(search.getPage(), wrapper);
        List<PlatformRoleEntity> roles = page.getRecords();
        List<PlatformRoleVo> roleVos = roles.stream().map(role -> {
            PlatformRoleVo roleVo = new PlatformRoleVo();
            BeanUtils.copyProperties(role, roleVo);
            return roleVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, roleVos);
    }
}