package com.hhchun.mall.user.platform.controller;


import com.hhchun.mall.common.constant.ValidationConstant.*;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.dto.PlatformUserRoleDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformUserRoleSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.user.platform.service.PlatformUserRoleService;
import com.hhchun.mall.common.utils.R;


/**
 * 平台用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/platform/user-role")
public class PlatformUserRoleController {
    @Autowired
    private PlatformUserRoleService platformUserRoleService;

    @PostMapping("/save")
    public R<?> savePlatformUserRoles(@RequestBody @Validated(ADD.class) PlatformUserRoleDto userRoleDto) {
        platformUserRoleService.savePlatformUserRoles(userRoleDto);

        return R.success();
    }

    @PostMapping("/roles")
    public R<PageResult<PlatformRoleVo>> getPlatformRoles(@RequestBody @Validated PlatformUserRoleSearchDto search) {
        PageResult<PlatformRoleVo> result = platformUserRoleService.getPlatformRoles(search);

        return R.success(result);
    }
}
