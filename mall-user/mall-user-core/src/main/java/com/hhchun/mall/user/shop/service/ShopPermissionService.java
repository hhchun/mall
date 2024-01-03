package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.entity.domain.ShopPermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopPermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopPermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.sun.istack.internal.NotNull;

/**
 * 店铺权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface ShopPermissionService extends IService<ShopPermissionEntity> {

    void saveShopPermission(ShopPermissionDto permissionDto);

    void modifyShopPermission(ShopPermissionDto permissionDto);

    ShopPermissionEntity getShopPermissionBySymbol(@NotNull String symbol);

    ShopPermissionEntity getShopPermissionById(@NotNull Long id);

    void removeShopPermission(Long permissionId);

    PageResult<ShopPermissionVo> getShopPermissionList(ShopPermissionSearchDto search);
}

