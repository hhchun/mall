package com.hhchun.mall.user.platform.controller;

import com.hhchun.mall.common.constant.ValidationConstant.*;
import com.hhchun.mall.user.platform.entity.dto.PlatformMenuDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hhchun.mall.user.platform.service.PlatformMenuService;
import com.hhchun.mall.common.utils.R;

import java.util.List;


/**
 * 平台菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@RestController
@RequestMapping("/user/platform/menu")
public class PlatformMenuController {
    @Autowired
    private PlatformMenuService platformMenuService;

    @PostMapping("/tree/{pid}")
    public R<List<PlatformMenuVo>> getPlatformMenuTree(@PathVariable(required = false) Long pid) {
        List<PlatformMenuVo> menuVos = platformMenuService.getPlatformMenuTree(pid);
        return R.success(menuVos);
    }

    @PostMapping("/save")
    public R<?> savePlatformMenu(@RequestBody @Validated(ADD.class) PlatformMenuDto menuDto) {
        platformMenuService.savePlatformMenu(menuDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyPlatformMenu(@RequestBody @Validated(UPDATE.class) PlatformMenuDto menuDto) {
        platformMenuService.modifyPlatformMenu(menuDto);

        return R.success();
    }

    @PostMapping("/remove/{menuId}")
    public R<?> removePlatformMenu(@PathVariable Long menuId) {
        platformMenuService.removePlatformMenu(menuId);

        return R.success();
    }

}
