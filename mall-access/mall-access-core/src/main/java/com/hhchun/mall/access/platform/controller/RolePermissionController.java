package com.hhchun.mall.access.platform.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhchun.mall.access.platform.entity.domain.RolePermissionEntity;
import com.hhchun.mall.access.platform.service.RolePermissionService;
import com.hhchun.mall.access.common.utils.R;


/**
 * 平台角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/rolepermission" )
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 保存
     */
    @RequestMapping("/save" )
    public R<?> save(@RequestBody RolePermissionEntity rolePermission) {
            rolePermissionService.save(rolePermission);

        return R.success();
    }

}
