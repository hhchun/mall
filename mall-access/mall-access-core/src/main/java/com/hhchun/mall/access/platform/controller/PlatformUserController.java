package com.hhchun.mall.access.platform.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhchun.mall.access.platform.entity.domain.PlatformUserEntity;
import com.hhchun.mall.access.platform.service.PlatformUserService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.common.utils.R;


/**
 * 平台用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/platformuser" )
public class PlatformUserController {
    @Autowired
    private PlatformUserService platformUserService;

    /**
     * 保存
     */
    @RequestMapping("/save" )
    public R<?> save(@RequestBody PlatformUserEntity platformUser) {
            platformUserService.save(platformUser);

        return R.success();
    }

}
