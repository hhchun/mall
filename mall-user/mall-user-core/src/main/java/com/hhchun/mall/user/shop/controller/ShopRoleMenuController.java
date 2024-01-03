package com.hhchun.mall.user.shop.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopRoleMenuDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleMenuSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopMenuVo;
import com.hhchun.mall.user.shop.service.ShopRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 店铺角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@RestController
@RequestMapping("/user/shop/role-menu")
public class ShopRoleMenuController {
    @Autowired
    private ShopRoleMenuService shopRoleMenuService;

    @PostMapping("/save")
    public R<?> saveShopRoleMenus(@RequestBody @Validated(ADD.class) ShopRoleMenuDto roleMenuDto) {
        shopRoleMenuService.saveShopRoleMenus(roleMenuDto);

        return R.success();
    }

    @PostMapping("/menu-tree")
    public R<List<ShopMenuVo>> getShopMenuTree(@RequestBody @Validated ShopRoleMenuSearchDto search) {
        List<ShopMenuVo> result = shopRoleMenuService.getShopMenuTree(search);

        return R.success(result);
    }
}
