package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.entity.domain.ShopRolePermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopRolePermissionDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRolePermissionSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopPermissionVo;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 店铺角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface ShopRolePermissionService extends IService<ShopRolePermissionEntity> {

    void saveShopRolePermissions(ShopRolePermissionDto rolePermissionDto);

    PageResult<ShopPermissionVo> getShopPermissions(ShopRolePermissionSearchDto search);

    List<Long> getShopRoleIdsByPermissionId(Long permissionId);

    /**
     * <p>
     * 根据权限id查询关联的所有角色id,包括已被删除(逻辑删除)的。
     * </p>
     *
     * @param permissionId 权限id
     * @return 角色id
     */
    @Nonnull
    List<Long> getShopRemovedRoleIdsByPermissionId(Long permissionId);

    @Nonnull
    List<Long> getShopPermissionIdsByRoleIds(Collection<Long> roleIds);
}

