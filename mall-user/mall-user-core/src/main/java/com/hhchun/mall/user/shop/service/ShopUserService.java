package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.entity.domain.ShopUserEntity;
import com.hhchun.mall.user.shop.entity.dto.LoginShopUserDto;
import com.hhchun.mall.user.shop.entity.dto.ShopUserDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopUserVo;
import com.sun.istack.internal.Nullable;

/**
 * 店铺用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface ShopUserService extends IService<ShopUserEntity> {

    void saveShopUser(ShopUserDto userDto);

    void modifyShopUser(ShopUserDto userDto);

    @Nullable
    ShopUserEntity getShopUserByUsername(String username);

    ShopUserEntity getShopUserById(Long id);

    PageResult<ShopUserVo> getShopUserList(ShopUserSearchDto search);

    String loginShopUser(LoginShopUserDto loginUserDto);


    // 根据用户id获取用户拥有的所有菜单数据
}

