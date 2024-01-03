package com.hhchun.mall.user.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.user.shop.entity.domain.ShopMenuEntity;
import com.hhchun.mall.user.shop.entity.dto.ShopMenuDto;
import com.hhchun.mall.user.shop.entity.vo.ShopMenuVo;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.List;


/**
 * 店铺菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */

public interface ShopMenuService extends IService<ShopMenuEntity> {

    List<ShopMenuVo> getShopMenuTree(@Nullable Long pid);

    @Nullable
    ShopMenuEntity getShopMenuBySymbol(String symbol);

    @Nullable
    ShopMenuEntity getShopMenuById(Long id);

    void saveShopMenu(ShopMenuDto menuDto);

    void modifyShopMenu(ShopMenuDto menuDto);

    void removeShopMenu(Long menuId);

    List<Long> getShopLowerMenuIdsByRoute(String route);

    @NotNull
    List<Long> getShopUpperMenuIdsByMenuIds(Collection<Long> menuIds);

    /**
     * <p>
     * 根据用户id获取用户拥有的菜单,当upper为true时,获取的结果包含上级菜单,否则反之。<br>
     * 当combine为true时,将多级菜单进行组合,下级菜单会保存在{@link ShopMenuVo#children}中,否则反之。<br>
     * 注意: combine为true时需要upper也为true才能生效。<br>
     * </p>
     *
     * @param userId  用户id
     * @param upper   是否包含上级菜单
     * @param combine 是否将多级菜单进行组合
     * @return 用户拥有的菜单
     */
    List<ShopMenuVo> getShopMenusByUserId(@Nullable Long userId, boolean upper, boolean combine);
}

