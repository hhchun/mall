package com.hhchun.mall.access.platform.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhchun.mall.access.platform.entity.domain.PlatformUserRoleEntity;
import com.hhchun.mall.access.platform.service.PlatformUserRoleService;
import com.hhchun.mall.access.common.utils.R;


/**
 * 平台用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/platformuserrole" )
public class PlatformUserRoleController {
    @Autowired
    private PlatformUserRoleService platformUserRoleService;

    /**
     * 保存
     */
    @RequestMapping("/save" )
    public R<?> save(@RequestBody PlatformUserRoleEntity platformUserRole) {
            platformUserRoleService.save(platformUserRole);

        return R.success();
    }

}
