package com.hhchun.mall.user.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.shop.entity.bo.ShopRoleBo;
import com.hhchun.mall.user.shop.entity.domain.ShopUserRoleEntity;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserRoleSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺用户关联角色
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface ShopUserRoleDao extends BaseMapper<ShopUserRoleEntity> {

    IPage<ShopRoleBo> getShopRoles(@Param("page") IPage<Object> page, @Param("search") ShopUserRoleSearchDto search);

    List<Long> getShopRemovedUserIdsByRoleIds(@Param("roleIds") List<Long> roleIds);
}
