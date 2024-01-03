package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.user.shop.entity.domain.ShopRoleMenuEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopRoleMenuDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopRoleMenuSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopMenuVo;

import java.util.Collection;
import java.util.List;

/**
 * 店铺角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface ShopRoleMenuService extends IService<ShopRoleMenuEntity> {

    void saveShopRoleMenus(ShopRoleMenuDto roleMenuDto);

    List<ShopMenuVo> getShopMenuTree(ShopRoleMenuSearchDto search);

    List<Long> getShopRoleIdsByMenuIds(List<Long> menuIds);

    List<Long> getShopRemovedRoleIdsByMenuIds(List<Long> menuIds);

    List<Long> getShopMenuIdsByRoleIds(Collection<Long> roleIds);
}

