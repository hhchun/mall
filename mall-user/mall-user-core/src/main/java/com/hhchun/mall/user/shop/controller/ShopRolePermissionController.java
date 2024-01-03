package com.hhchun.mall.user.shop.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopRolePermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRolePermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.hhchun.mall.user.shop.service.ShopRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/shop/role-permission")
public class ShopRolePermissionController {
    @Autowired
    private ShopRolePermissionService shopRolePermissionService;

    @PostMapping("/save")
    public R<?> saveShopRolePermissions(@RequestBody @Validated(ADD.class) ShopRolePermissionDto rolePermissionDto) {
        shopRolePermissionService.saveShopRolePermissions(rolePermissionDto);

        return R.success();
    }

    @PostMapping("/permissions")
    public R<PageResult<ShopPermissionVo>> getShopPermissions(@RequestBody @Validated ShopRolePermissionSearchDto search) {
        PageResult<ShopPermissionVo> result = shopRolePermissionService.getShopPermissions(search);

        return R.success(result);
    }

}
