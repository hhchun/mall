package com.hhchun.mall.user.platform.controller;

import com.hhchun.mall.common.constant.ValidationConstant.*;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.dto.PlatformRoleDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRoleSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.user.platform.service.PlatformRoleService;
import com.hhchun.mall.common.utils.R;


/**
 * 平台角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/platform/role")
public class PlatformRoleController {
    @Autowired
    private PlatformRoleService platformRoleService;

    @PostMapping("/save")
    public R<?> savePlatformRole(@RequestBody @Validated(ADD.class) PlatformRoleDto roleDto) {
        platformRoleService.savePlatformRole(roleDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyPlatformRole(@RequestBody @Validated(UPDATE.class) PlatformRoleDto roleDto) {
        platformRoleService.modifyPlatformRole(roleDto);

        return R.success();
    }

    @PostMapping("/remove/{roleId}")
    public R<?> removePlatformRole(@PathVariable Long roleId) {
        platformRoleService.removePlatformRole(roleId);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<PlatformRoleVo>> getPlatformRoleList(@RequestBody PlatformRoleSearchDto search) {
        PageResult<PlatformRoleVo> result = platformRoleService.getPlatformRoleList(search);
        return R.success(result);
    }

}
