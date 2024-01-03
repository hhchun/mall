package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuPermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopMenuPermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopMenuPermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;
import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.List;


/**
 * 店铺菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface ShopMenuPermissionService extends IService<ShopMenuPermissionEntity> {

    void saveMenuPermissions(ShopMenuPermissionDto menuPermissionDto);

    PageResult<ShopPermissionVo> getShopPermissions(ShopMenuPermissionSearchDto search);

    @NotNull
    List<Long> getShopPermissionIdsByMenuIds(Collection<Long> menuIds);
}

