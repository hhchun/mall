package com.hhchun.mall.user.platform.controller;


import com.hhchun.mall.common.constant.ValidationConstant.*;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.dto.PlatformRolePermissionDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.user.platform.service.PlatformRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.common.utils.R;

/**
 * 平台角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/access/permission/role-permission")
public class PlatformRolePermissionController {
    @Autowired
    private PlatformRolePermissionService platformRolePermissionService;

    @PostMapping("/save")
    public R<?> savePlatformRolePermissions(@RequestBody @Validated(ADD.class) PlatformRolePermissionDto rolePermissionDto) {
        platformRolePermissionService.savePlatformRolePermissions(rolePermissionDto);

        return R.success();
    }

    @PostMapping("/permissions")
    public R<PageResult<PlatformPermissionVo>> getPlatformPermissions(@RequestBody @Validated PlatformRolePermissionSearchDto search) {
        PageResult<PlatformPermissionVo> result = platformRolePermissionService.getPlatformPermissions(search);

        return R.success(result);
    }

}
