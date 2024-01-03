package com.hhchun.mall.user.shop.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.ShopUserRoleDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserRoleSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopRoleVo;
import com.hhchun.mall.user.shop.service.ShopUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/shop/user-role")
public class ShopUserRoleController {
    @Autowired
    private ShopUserRoleService shopUserRoleService;

    @PostMapping("/save")
    public R<?> saveShopUserRoles(@RequestBody @Validated(ADD.class) ShopUserRoleDto userRoleDto) {
        shopUserRoleService.saveShopUserRoles(userRoleDto);

        return R.success();
    }

    @PostMapping("/roles")
    public R<PageResult<ShopRoleVo>> getShopRoles(@RequestBody @Validated ShopUserRoleSearchDto search) {
        PageResult<ShopRoleVo> result = shopUserRoleService.getShopRoles(search);

        return R.success(result);
    }
}
