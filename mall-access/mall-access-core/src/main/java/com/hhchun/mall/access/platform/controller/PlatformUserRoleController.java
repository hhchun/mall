package com.hhchun.mall.access.platform.controller;


import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.dto.PlatformUserRoleDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformUserRoleSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/access/platform/user-role")
public class PlatformUserRoleController {
    @Autowired
    private PlatformUserRoleService platformUserRoleService;

    @PostMapping("/saves")
    public R<?> savePlatformUserRoles(@RequestBody @Validated(ADD.class) PlatformUserRoleDto userRoleDto) {
        platformUserRoleService.savePlatformUserRoles(userRoleDto);

        return R.success();
    }

    @PostMapping("/remove/{userRoleId}")
    public R<?> removePlatformUserRole(@PathVariable Long userRoleId) {
        platformUserRoleService.removePlatformUserRole(userRoleId);

        return R.success();
    }

    @PostMapping("/bound-role-list")
    public R<PageResult<PlatformRoleVo>> getPlatformBoundRoleList(@RequestBody PlatformUserRoleSearchDto search) {
        PageResult<PlatformRoleVo> result = platformUserRoleService.getPlatformBoundRoleList(search);

        return R.success(result);
    }

    @PostMapping("/unbound-role-list")
    public R<PageResult<PlatformRoleVo>> getPlatformUnboundRoleList(@RequestBody PlatformUserRoleSearchDto search) {
        PageResult<PlatformRoleVo> result = platformUserRoleService.getPlatformUnboundRoleList(search);

        return R.success(result);
    }

}
