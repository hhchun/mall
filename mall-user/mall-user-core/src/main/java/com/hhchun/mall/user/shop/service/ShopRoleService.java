package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopRoleDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopRoleVo;
import com.sun.istack.internal.NotNull;

/**
 * 店铺角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface ShopRoleService extends IService<ShopRoleEntity> {

    void saveShopRole(ShopRoleDto roleDto);

    ShopRoleEntity getShopRoleBySymbol(@NotNull String symbol);

    void modifyShopRole(ShopRoleDto roleDto);

    ShopRoleEntity getShopRoleById(@NotNull Long id);

    void removeShopRole(Long roleId);

    PageResult<ShopRoleVo> getShopRoleList(ShopRoleSearchDto search);
}

