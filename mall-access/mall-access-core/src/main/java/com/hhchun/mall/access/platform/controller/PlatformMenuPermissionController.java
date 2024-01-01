package com.hhchun.mall.access.platform.controller;


import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.dto.PlatformMenuPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformMenuPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/access/platform/menu-permission")
public class PlatformMenuPermissionController {
    @Autowired
    private PlatformMenuPermissionService platformMenuPermissionService;

    @PostMapping("/save")
    public R<?> saveMenuPermissions(@RequestBody @Validated(ADD.class) PlatformMenuPermissionDto menuPermissionDto) {
        platformMenuPermissionService.saveMenuPermissions(menuPermissionDto);

        return R.success();
    }

    @PostMapping("/permissions")
    public R<PageResult<PlatformPermissionVo>> getPlatformPermissions(@RequestBody @Validated PlatformMenuPermissionSearchDto search) {
        PageResult<PlatformPermissionVo> result = platformMenuPermissionService.getPlatformPermissions(search);

        return R.success(result);
    }

}
