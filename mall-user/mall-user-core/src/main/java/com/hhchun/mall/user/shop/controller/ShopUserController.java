package com.hhchun.mall.user.shop.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.shop.entity.dto.LoginShopUserDto;
import com.hhchun.mall.user.shop.entity.dto.ShopUserDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopUserVo;
import com.hhchun.mall.user.shop.service.ShopUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/shop/user")
public class ShopUserController {
    @Autowired
    private ShopUserService shopUserService;

    @PostMapping("/save")
    public R<?> saveShopUser(@RequestBody @Validated(ADD.class) ShopUserDto userDto) {
        shopUserService.saveShopUser(userDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyShopUser(@RequestBody @Validated(UPDATE.class) ShopUserDto userDto) {
        shopUserService.modifyShopUser(userDto);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<ShopUserVo>> getShopUserList(@RequestBody ShopUserSearchDto search) {
        PageResult<ShopUserVo> result = shopUserService.getShopUserList(search);

        return R.success(result);
    }

    @PostMapping("/login")
    public R<String> loginShopUser(@RequestBody @Validated LoginShopUserDto loginUserDto) {
        String token = shopUserService.loginShopUser(loginUserDto);

        return R.success(token);
    }
}
