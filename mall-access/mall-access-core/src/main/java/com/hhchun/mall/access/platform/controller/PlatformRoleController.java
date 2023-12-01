package com.hhchun.mall.access.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhchun.mall.access.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.access.platform.service.PlatformRoleService;
import com.hhchun.mall.access.common.utils.R;


/**
 * 平台角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/platformrole" )
public class PlatformRoleController {
    @Autowired
    private PlatformRoleService platformRoleService;

    /**
     * 保存
     */
    @RequestMapping("/save" )
    public R<?> save(@RequestBody PlatformRoleEntity platformRole) {
            platformRoleService.save(platformRole);

        return R.success();
    }

}
