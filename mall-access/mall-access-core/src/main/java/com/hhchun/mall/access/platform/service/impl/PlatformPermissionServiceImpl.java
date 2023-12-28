package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.dao.PlatformPermissionDao;
import com.hhchun.mall.access.platform.entity.dto.PlatformPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.access.platform.event.Action;
import com.hhchun.mall.access.platform.event.PlatformPermissionEvent;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.hhchun.mall.access.platform.entity.domain.PlatformPermissionEntity;
import com.hhchun.mall.access.platform.service.PlatformPermissionService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("platformPermissionService")
public class PlatformPermissionServiceImpl extends ServiceImpl<PlatformPermissionDao, PlatformPermissionEntity> implements PlatformPermissionService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void savePlatformPermission(PlatformPermissionDto permissionDto) {
        String symbol = permissionDto.getSymbol();
        PlatformPermissionEntity permission = getPlatformPermissionBySymbol(symbol);
        Preconditions.checkCondition(permission == null, "权限标识已存在");
        permission = new PlatformPermissionEntity();
        BeanUtils.copyProperties(permissionDto, permission);
        save(permission);
        publisher.publishEvent(new PlatformPermissionEvent(this, Action.SAVE, permission.getId()));
    }

    @Override
    public void modifyPlatformPermission(PlatformPermissionDto permissionDto) {
        Long permissionId = permissionDto.getId();
        PlatformPermissionEntity permission = getPlatformPermissionById(permissionId);
        Preconditions.checkCondition(permission != null, "修改的权限不存在");
        String symbol = permissionDto.getSymbol();
        if (StringUtils.hasLength(symbol)) {
            PlatformPermissionEntity symbolPermission = getPlatformPermissionBySymbol(symbol);
            Preconditions.checkCondition(symbolPermission == null
                            || Objects.equals(symbolPermission.getId(), permissionId),
                    "权限标识已存在");
        }
        PlatformPermissionEntity modifyPermission = new PlatformPermissionEntity();
        BeanUtils.copyProperties(permissionDto, modifyPermission);
        updateById(modifyPermission);
        publisher.publishEvent(new PlatformPermissionEvent(this, Action.MODIFY, permissionId));
    }

    @Nullable
    @Override
    public PlatformPermissionEntity getPlatformPermissionBySymbol(@NotNull String symbol) {
        Preconditions.checkArgument(StringUtils.hasLength(symbol), "symbol is empty!");
        return getOne(new LambdaQueryWrapper<PlatformPermissionEntity>()
                .eq(PlatformPermissionEntity::getSymbol, symbol));
    }

    @Nullable
    @Override
    public PlatformPermissionEntity getPlatformPermissionById(@NotNull Long id) {
        Preconditions.checkArgument(id != null, "id is null!");
        return getById(id);
    }

    @Override
    public void removePlatformPermission(Long permissionId) {
        removeById(permissionId);
        publisher.publishEvent(new PlatformPermissionEvent(this, Action.REMOVE, permissionId));
    }

    @Override
    public PageResult<PlatformPermissionVo> getPlatformPermissionList(PlatformPermissionSearchDto search) {
        Long id = search.getId();
        String symbol = search.getSymbol();
        Integer overt = search.getOvert();
        String name = search.getName();
        String subject = search.getSubject();

        LambdaQueryWrapper<PlatformPermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id != null, PlatformPermissionEntity::getId, id);
        wrapper.eq(StringUtils.hasLength(symbol), PlatformPermissionEntity::getSymbol, symbol);
        wrapper.like(StringUtils.hasLength(name), PlatformPermissionEntity::getName, name);
        wrapper.like(StringUtils.hasLength(subject), PlatformPermissionEntity::getSubject, subject);
        wrapper.eq(overt != null, PlatformPermissionEntity::getOvert, overt);
        IPage<PlatformPermissionEntity> page = page(search.getPage(), wrapper);
        List<PlatformPermissionEntity> permissions = page.getRecords();
        List<PlatformPermissionVo> permissionVos = permissions.stream().map(permission -> {
            PlatformPermissionVo permissionVo = new PlatformPermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            return permissionVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, permissionVos);
    }
}