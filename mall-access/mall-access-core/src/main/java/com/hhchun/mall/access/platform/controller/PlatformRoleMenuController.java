package com.hhchun.mall.access.platform.controller;


import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.dto.PlatformRoleMenuDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformMenuVo;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.access.platform.service.PlatformRoleMenuService;

import com.hhchun.mall.access.common.utils.R;


/**
 * 平台角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@RestController
@RequestMapping("/access/platform/role-menu")
public class PlatformRoleMenuController {
    @Autowired
    private PlatformRoleMenuService platformRoleMenuService;

    @PostMapping("/saves")
    public R<?> savePlatformRoleMenus(@RequestBody @Validated(ADD.class) PlatformRoleMenuDto roleMenuDto) {
        platformRoleMenuService.savePlatformRoleMenus(roleMenuDto);

        return R.success();
    }

    @PostMapping("/remove/{roleMenuId}")
    public R<?> removePlatformRoleMenu(@PathVariable Long roleMenuId) {
        platformRoleMenuService.removePlatformRoleMenu(roleMenuId);

        return R.success();
    }


    @PostMapping("/bound-menu-list")
    public R<PageResult<PlatformMenuVo>> getPlatformBoundMenuList(@RequestBody PlatformRoleMenuSearchDto search) {
        PageResult<PlatformMenuVo> result = platformRoleMenuService.getPlatformBoundMenuList(search);

        return R.success(result);
    }


    @PostMapping("/unbound-menu-list")
    public R<PageResult<PlatformMenuVo>> getPlatformUnboundMenuList(@RequestBody PlatformRoleMenuSearchDto search) {
        PageResult<PlatformMenuVo> result = platformRoleMenuService.getPlatformUnboundMenuList(search);

        return R.success(result);
    }

}
