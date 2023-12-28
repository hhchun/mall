package com.hhchun.mall.access.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.dao.PlatformRoleMenuDao;
import com.hhchun.mall.access.platform.entity.domain.*;
import com.hhchun.mall.access.platform.entity.dto.PlatformRoleMenuDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformMenuVo;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.access.platform.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service("platformRoleMenuService")
public class PlatformRoleMenuServiceImpl extends ServiceImpl<PlatformRoleMenuDao, PlatformRoleMenuEntity> implements PlatformRoleMenuService {

    @Autowired
    private PlatformRoleService platformRoleService;
    @Autowired
    private PlatformMenuService platformMenuService;
    @Autowired
    private PlatformPermissionService platformPermissionService;

    @Override
    @Transactional
    public void savePlatformRoleMenus(PlatformRoleMenuDto roleMenuDto) {
        Long roleId = roleMenuDto.getRoleId();
        Set<Long> menuIds = roleMenuDto.getMenuIds();
        PlatformRoleEntity role = platformRoleService.getPlatformRoleById(roleId);
        Preconditions.checkCondition(role != null, "角色不存在");
        Set<Long> existMenuIds = list(new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .select(PlatformRoleMenuEntity::getMenuId)
                .eq(PlatformRoleMenuEntity::getRoleId, roleId)
                .in(PlatformRoleMenuEntity::getMenuId, menuIds))
                .stream().map(PlatformRoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());
        // 过滤掉已存在的
        menuIds = Sets.difference(menuIds, existMenuIds);

        List<PlatformRoleMenuEntity> roleMenus = menuIds.stream().map(menuId -> {
            PlatformRoleMenuEntity roleMenu = new PlatformRoleMenuEntity();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            return roleMenu;
        }).collect(Collectors.toList());
        saveBatch(roleMenus);
    }

    @Override
    public void removePlatformRoleMenu(Long roleMenuId) {
        removeById(roleMenuId);
    }

    @Override
    public PageResult<PlatformMenuVo> getPlatformBoundMenuList(PlatformRoleMenuSearchDto search) {
        Long roleId = search.getRoleId();
        Set<Long> menuIds = list(new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .select(PlatformRoleMenuEntity::getMenuId)
                .eq(PlatformRoleMenuEntity::getRoleId, roleId))
                .stream().map(PlatformRoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(menuIds)) {
            return PageResult.empty();
        }
        IPage<PlatformMenuEntity> page = platformMenuService.page(search.getPage(),
                new LambdaQueryWrapper<PlatformMenuEntity>().in(PlatformMenuEntity::getId, menuIds));
        List<PlatformMenuEntity> menus = page.getRecords();
        List<PlatformMenuVo> menuVos = menus.stream().map(menu -> {
            PlatformMenuVo menuVo = new PlatformMenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, menuVos);
    }

    @Override
    public PageResult<PlatformMenuVo> getPlatformUnboundMenuList(PlatformRoleMenuSearchDto search) {
        Long roleId = search.getRoleId();
        Set<Long> menuIds = list(new LambdaQueryWrapper<PlatformRoleMenuEntity>()
                .select(PlatformRoleMenuEntity::getMenuId)
                .eq(PlatformRoleMenuEntity::getRoleId, roleId))
                .stream().map(PlatformRoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());

        LambdaQueryWrapper<PlatformMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.notIn(!CollectionUtils.isEmpty(menuIds), PlatformMenuEntity::getId, menuIds);
        IPage<PlatformMenuEntity> page = platformMenuService.page(search.getPage(), wrapper);
        List<PlatformMenuEntity> menus = page.getRecords();
        List<PlatformMenuVo> menuVos = menus.stream().map(menu -> {
            PlatformMenuVo menuVo = new PlatformMenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());

        return PageResult.convert(page, menuVos);
    }

    @Override
    public List<Long> getPlatformRoleIdsByMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            return null;
        }
        LambdaQueryWrapper<PlatformRoleMenuEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(PlatformRoleMenuEntity::getRoleId);
        wrapper.in(PlatformRoleMenuEntity::getMenuId, menuIds);
        return list(wrapper).stream()
                .map(PlatformRoleMenuEntity::getRoleId)
                .collect(Collectors.toList());
    }
}