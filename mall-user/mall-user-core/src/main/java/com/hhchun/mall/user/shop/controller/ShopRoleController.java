package com.hhchun.mall.user.shop.controller;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopRoleDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopRoleVo;
import com.hhchun.mall.user.shop.service.ShopRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 店铺角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/shop/role")
public class ShopRoleController {
    @Autowired
    private ShopRoleService shopRoleService;

    @PostMapping("/save")
    public R<?> saveShopRole(@RequestBody @Validated(ADD.class) ShopRoleDto roleDto) {
        shopRoleService.saveShopRole(roleDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyShopRole(@RequestBody @Validated(UPDATE.class) ShopRoleDto roleDto) {
        shopRoleService.modifyShopRole(roleDto);

        return R.success();
    }

    @PostMapping("/remove/{roleId}")
    public R<?> removeShopRole(@PathVariable Long roleId) {
        shopRoleService.removeShopRole(roleId);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<ShopRoleVo>> getShopRoleList(@RequestBody ShopRoleSearchDto search) {
        PageResult<ShopRoleVo> result = shopRoleService.getShopRoleList(search);
        return R.success(result);
    }

}
