package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.platform.constant.PlatformMenuConstant;
import com.hhchun.mall.access.platform.dao.PlatformMenuDao;
import com.hhchun.mall.access.platform.entity.dto.PlatformMenuDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformMenuVo;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuEntity;
import com.hhchun.mall.access.platform.service.PlatformMenuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("platformMenuService")
public class PlatformMenuServiceImpl extends ServiceImpl<PlatformMenuDao, PlatformMenuEntity> implements PlatformMenuService {

    @Override
    public List<PlatformMenuVo> getPlatformMenuTree(@Nullable Long pid) {
        pid = Optional.ofNullable(pid).orElse(0L);
        List<PlatformMenuEntity> menus = list(new LambdaQueryWrapper<PlatformMenuEntity>().eq(PlatformMenuEntity::getPid, pid));
        return menus.stream().map(menu -> {
            PlatformMenuVo menuVo = new PlatformMenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());
    }

    @Override
    public void savePlatformMenu(PlatformMenuDto menuDto) {
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        PlatformMenuEntity sm = getPlatformMenuBySymbol(symbol);
        Preconditions.checkCondition(sm == null, "菜单标识已存在");
        if (!Objects.equals(PlatformMenuConstant.MENU_ROOT_PID, pid)) {
            PlatformMenuEntity pm = getPlatformMenuById(pid);
            Preconditions.checkCondition(pm != null, "父菜单不存在");
        }
        PlatformMenuEntity menu = new PlatformMenuEntity();
        BeanUtils.copyProperties(menuDto, menu);
        save(menu);
    }

    @Nullable
    public PlatformMenuEntity getPlatformMenuById(Long id) {
        return getById(id);
    }

    @Nullable
    @Override
    public PlatformMenuEntity getPlatformMenuBySymbol(String symbol) {
        return getOne(new LambdaQueryWrapper<PlatformMenuEntity>()
                .eq(PlatformMenuEntity::getSymbol, symbol));
    }

    @Override
    public void modifyPlatformMenu(PlatformMenuDto menuDto) {
        Long id = menuDto.getId();
        String symbol = menuDto.getSymbol();
        Long pid = menuDto.getPid();
        PlatformMenuEntity menu = getPlatformMenuById(id);
        Preconditions.checkCondition(menu != null, "菜单不存在");
        if (StringUtils.hasLength(symbol) && !symbol.equals(menu.getSymbol())) {
            PlatformMenuEntity sm = getPlatformMenuBySymbol(symbol);
            Preconditions.checkCondition(sm == null, "菜单标识已存在");
        }
        if (pid != null && !pid.equals(menu.getPid())) {
            PlatformMenuEntity pm = getPlatformMenuById(pid);
            Preconditions.checkCondition(pm != null, "父菜单不存在");
        }
        BeanUtils.copyProperties(menuDto, menu);
        updateById(menu);
    }

    @Override
    @Transactional
    public void removePlatformMenu(Long menuId) {
        // 递归删除
        List<Long> menuIds = Lists.newArrayList(menuId);
        while (!CollectionUtils.isEmpty(menuIds)) {
            List<PlatformMenuEntity> menus = list(new LambdaQueryWrapper<PlatformMenuEntity>()
                    .select(PlatformMenuEntity::getId)
                    .in(PlatformMenuEntity::getPid, menuIds));
            removeBatchByIds(menuIds);
            menuIds = menus.stream().map(PlatformMenuEntity::getId).collect(Collectors.toList());
        }
    }
}