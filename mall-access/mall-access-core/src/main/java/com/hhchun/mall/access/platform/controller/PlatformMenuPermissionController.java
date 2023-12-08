package com.hhchun.mall.access.platform.controller;


import com.hhchun.mall.access.platform.entity.domain.PlatformMenuPermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhchun.mall.access.platform.service.PlatformMenuPermissionService;
import com.hhchun.mall.access.common.utils.R;


/**
 * 平台菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@RestController
@RequestMapping("/platformmenupermission" )
public class PlatformMenuPermissionController {
    @Autowired
    private PlatformMenuPermissionService platformMenuPermissionService;

    /**
     * 保存
     */
    @RequestMapping("/save" )
    public R<?> save(@RequestBody PlatformMenuPermissionEntity platformMenuPermission) {
            platformMenuPermissionService.save(platformMenuPermission);

        return R.success();
    }

}
