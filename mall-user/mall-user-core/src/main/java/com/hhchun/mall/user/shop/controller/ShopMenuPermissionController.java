package com.hhchun.mall.user.shop.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopMenuPermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopMenuPermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.hhchun.mall.user.shop.service.ShopMenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@RestController
@RequestMapping("/user/shop/menu-permission")
public class ShopMenuPermissionController {
    @Autowired
    private ShopMenuPermissionService shopMenuPermissionService;

    @PostMapping("/save")
    public R<?> saveMenuPermissions(@RequestBody @Validated(ADD.class) ShopMenuPermissionDto menuPermissionDto) {
        shopMenuPermissionService.saveMenuPermissions(menuPermissionDto);

        return R.success();
    }

    @PostMapping("/permissions")
    public R<PageResult<ShopPermissionVo>> getShopPermissions(@RequestBody @Validated ShopMenuPermissionSearchDto search) {
        PageResult<ShopPermissionVo> result = shopMenuPermissionService.getShopPermissions(search);

        return R.success(result);
    }

}
