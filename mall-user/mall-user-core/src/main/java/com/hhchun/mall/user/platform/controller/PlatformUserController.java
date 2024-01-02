package com.hhchun.mall.user.platform.controller;


import com.hhchun.mall.common.constant.ValidationConstant.*;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.dto.LoginPlatformUserDto;
import com.hhchun.mall.user.platform.entity.dto.PlatformUserDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformUserSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hhchun.mall.user.platform.service.PlatformUserService;
import com.hhchun.mall.common.utils.R;


/**
 * 平台用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/access/platform/user")
public class PlatformUserController {
    @Autowired
    private PlatformUserService platformUserService;

    @PostMapping("/save")
    public R<?> savePlatformUser(@RequestBody @Validated(ADD.class) PlatformUserDto userDto) {
        platformUserService.savePlatformUser(userDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyPlatformUser(@RequestBody @Validated(UPDATE.class) PlatformUserDto userDto) {
        platformUserService.modifyPlatformUser(userDto);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<PlatformUserVo>> getPlatformUserList(@RequestBody PlatformUserSearchDto search) {
        PageResult<PlatformUserVo> result = platformUserService.getPlatformUserList(search);

        return R.success(result);
    }

    @PostMapping("/login")
    public R<String> loginPlatformUser(@RequestBody @Validated LoginPlatformUserDto loginUserDto) {
        String token = platformUserService.loginPlatformUser(loginUserDto);

        return R.success(token);
    }
}
