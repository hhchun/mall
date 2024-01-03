package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.shop.entity.domain.ShopUserRoleEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopUserRoleDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserRoleSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopRoleVo;

import java.util.List;


/**
 * 店铺用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface ShopUserRoleService extends IService<ShopUserRoleEntity> {

    void saveShopUserRoles(ShopUserRoleDto userRoleDto);

    PageResult<ShopRoleVo> getShopRoles(ShopUserRoleSearchDto search);

    List<Long> getShopUserIdsByRoleIds(List<Long> roleIds);

    List<Long> getShopRemovedUserIdsByRoleIds(List<Long> roleIds);

    List<Long> getShopRoleIdsByUserId(Long userId);
}

