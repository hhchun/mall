package com.hhchun.mall.user.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.shop.entity.bo.ShopPermissionBo;
import com.hhchun.mall.user.shop.entity.domain.ShopRolePermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRolePermissionSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺角色关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface ShopRolePermissionDao extends BaseMapper<ShopRolePermissionEntity> {

    IPage<ShopPermissionBo> getShopPermissions(@Param("page") IPage<Object> page, @Param("search") ShopRolePermissionSearchDto search);

    List<Long> getShopRemovedRoleIdsByPermissionId(@Param("permissionId") Long permissionId);
}
