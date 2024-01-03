package com.hhchun.mall.user.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.shop.entity.bo.ShopPermissionBo;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuPermissionEntity;
import com.hhchun.mall.user.shop.entity.dto.search.ShopMenuPermissionSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 店铺菜单关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Mapper
public interface ShopMenuPermissionDao extends BaseMapper<ShopMenuPermissionEntity> {

    IPage<ShopPermissionBo> getShopPermissions(@Param("page") IPage<Object> page,
                                                       @Param("search") ShopMenuPermissionSearchDto search);
}
