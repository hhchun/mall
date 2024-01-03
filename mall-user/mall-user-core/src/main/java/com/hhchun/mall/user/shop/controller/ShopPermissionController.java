package com.hhchun.mall.user.shop.controller;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopPermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopPermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.hhchun.mall.user.shop.service.ShopPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 店铺权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/shop/permission")
public class ShopPermissionController {
    @Autowired
    private ShopPermissionService shopPermissionService;

    @PostMapping("/save")
    public R<?> saveShopPermission(@RequestBody @Validated(ADD.class) ShopPermissionDto permissionDto) {
        shopPermissionService.saveShopPermission(permissionDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyShopPermission(@RequestBody @Validated(UPDATE.class) ShopPermissionDto permissionDto) {
        shopPermissionService.modifyShopPermission(permissionDto);

        return R.success();
    }

    @PostMapping("/remove/{permissionId}")
    public R<?> removeShopPermission(@PathVariable Long permissionId) {
        shopPermissionService.removeShopPermission(permissionId);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<ShopPermissionVo>> getShopPermissionList(@RequestBody ShopPermissionSearchDto search) {
        PageResult<ShopPermissionVo> result = shopPermissionService.getShopPermissionList(search);
        return R.success(result);
    }
}
