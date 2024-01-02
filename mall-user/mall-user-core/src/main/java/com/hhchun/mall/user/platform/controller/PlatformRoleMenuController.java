package com.hhchun.mall.user.platform.controller;


import com.hhchun.mall.common.constant.ValidationConstant.*;
import com.hhchun.mall.user.platform.entity.dto.PlatformRoleMenuDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.user.platform.service.PlatformRoleMenuService;

import com.hhchun.mall.common.utils.R;

import java.util.List;


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

    @PostMapping("/save")
    public R<?> savePlatformRoleMenus(@RequestBody @Validated(ADD.class) PlatformRoleMenuDto roleMenuDto) {
        platformRoleMenuService.savePlatformRoleMenus(roleMenuDto);

        return R.success();
    }

    @PostMapping("/menu-tree")
    public R<List<PlatformMenuVo>> getPlatformMenuTree(@RequestBody @Validated PlatformRoleMenuSearchDto search) {
        List<PlatformMenuVo> result = platformRoleMenuService.getPlatformMenuTree(search);

        return R.success(result);
    }
}
