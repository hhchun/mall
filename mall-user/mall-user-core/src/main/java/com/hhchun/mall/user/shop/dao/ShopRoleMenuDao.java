package com.hhchun.mall.user.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.user.shop.entity.bo.ShopMenuBo;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleMenuEntity;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleMenuSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Mapper
public interface ShopRoleMenuDao extends BaseMapper<ShopRoleMenuEntity> {

    List<ShopMenuBo> getShopMenuTree(@Param("search") ShopRoleMenuSearchDto search);

    List<Long> getShopRemovedRoleIdsByMenuIds(@Param("menuIds") List<Long> menuIds);
}
