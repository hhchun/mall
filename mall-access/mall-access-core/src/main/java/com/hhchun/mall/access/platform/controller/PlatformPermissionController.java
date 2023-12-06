package com.hhchun.mall.access.platform.controller;

import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.dto.PlatformPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.access.platform.service.PlatformPermissionService;
import com.hhchun.mall.access.common.utils.R;


/**
 * 平台权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/access/platform/permission")
public class PlatformPermissionController {
    @Autowired
    private PlatformPermissionService platformPermissionService;

    @PostMapping("/save")
    public R<?> savePlatformPermission(@RequestBody @Validated(ADD.class) PlatformPermissionDto permissionDto) {
        platformPermissionService.savePlatformPermission(permissionDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyPlatformPermission(@RequestBody @Validated(UPDATE.class) PlatformPermissionDto permissionDto) {
        platformPermissionService.modifyPlatformPermission(permissionDto);

        return R.success();
    }

    @PostMapping("/remove/{permissionId}")
    public R<?> removePlatformPermission(@PathVariable Long permissionId) {
        platformPermissionService.removePlatformPermission(permissionId);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<PlatformPermissionVo>> getPlatformPermissionList(@RequestBody PlatformPermissionSearchDto search) {
        PageResult<PlatformPermissionVo> result = platformPermissionService.getPlatformPermissionList(search);
        return R.success(result);
    }
}
