package com.hhchun.mall.access.platform.controller;


import com.hhchun.mall.access.platform.entity.domain.PlatformRoleMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/access/platform/romenule-menu")
public class PlatformRoleMenuController {
    @Autowired
    private PlatformRoleMenuService platformRoleMenuService;

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R<?> save(@RequestBody PlatformRoleMenuEntity platformRoleMenu) {
        platformRoleMenuService.save(platformRoleMenu);

        return R.success();
    }

}
