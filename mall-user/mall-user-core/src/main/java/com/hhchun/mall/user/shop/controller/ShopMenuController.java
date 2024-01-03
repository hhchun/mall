package com.hhchun.mall.user.shop.controller;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopMenuDto;
import com.hhchun.mall.user.shop.entity.vo.ShopMenuVo;
import com.hhchun.mall.user.shop.service.ShopMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 店铺菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@RestController
@RequestMapping("/user/shop/menu")
public class ShopMenuController {
    @Autowired
    private ShopMenuService shopMenuService;

    @PostMapping("/tree/{pid}")
    public R<List<ShopMenuVo>> getShopMenuTree(@PathVariable(required = false) Long pid) {
        List<ShopMenuVo> menuVos = shopMenuService.getShopMenuTree(pid);
        return R.success(menuVos);
    }

    @PostMapping("/save")
    public R<?> saveShopMenu(@RequestBody @Validated(ADD.class) ShopMenuDto menuDto) {
        shopMenuService.saveShopMenu(menuDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyShopMenu(@RequestBody @Validated(UPDATE.class) ShopMenuDto menuDto) {
        shopMenuService.modifyShopMenu(menuDto);

        return R.success();
    }

    @PostMapping("/remove/{menuId}")
    public R<?> removeShopMenu(@PathVariable Long menuId) {
        shopMenuService.removeShopMenu(menuId);

        return R.success();
    }

}
