package com.hhchun.mall.access.platform.controller;


import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.dto.PlatformRolePermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.hhchun.mall.access.platform.service.PlatformRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.access.common.utils.R;

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

    @PostMapping("/saves")
    public R<?> savePlatformRolePermissions(@RequestBody @Validated(ADD.class) PlatformRolePermissionDto rolePermissionDto) {
        platformRolePermissionService.savePlatformRolePermissions(rolePermissionDto);

        return R.success();
    }

    @PostMapping("/remove/{rolePermissionId}")
    public R<?> removePlatformRolePermission(@PathVariable Long rolePermissionId) {
        platformRolePermissionService.removePlatformRolePermission(rolePermissionId);

        return R.success();
    }

    @PostMapping("/bound-permission-list")
    public R<PageResult<PlatformPermissionVo>> getPlatformBoundPermissionList(@RequestBody PlatformRolePermissionSearchDto search) {
        PageResult<PlatformPermissionVo> result = platformRolePermissionService.getPlatformBoundPermissionList(search);

        return R.success(result);
    }

    @PostMapping("/unbound-permission-list")
    public R<PageResult<PlatformPermissionVo>> getPlatformUnboundPermissionList(@RequestBody PlatformRolePermissionSearchDto search) {
        PageResult<PlatformPermissionVo> result = platformRolePermissionService.getPlatformUnboundPermissionList(search);

        return R.success(result);
    }


}
