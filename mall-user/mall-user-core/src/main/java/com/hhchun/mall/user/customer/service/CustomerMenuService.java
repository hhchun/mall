package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerMenuDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerMenuVo;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.List;


/**
 * 顾客菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */

public interface CustomerMenuService extends IService<CustomerMenuEntity> {

    List<CustomerMenuVo> getCustomerMenuTree(@Nullable Long pid);

    @Nullable
    CustomerMenuEntity getCustomerMenuBySymbol(String symbol);

    @Nullable
    CustomerMenuEntity getCustomerMenuById(Long id);

    void saveCustomerMenu(CustomerMenuDto menuDto);

    void modifyCustomerMenu(CustomerMenuDto menuDto);

    void removeCustomerMenu(Long menuId);

    List<Long> getCustomerLowerMenuIdsByRoute(String route);

    @NotNull
    List<Long> getCustomerUpperMenuIdsByMenuIds(Collection<Long> menuIds);

    /**
     * <p>
     * 根据用户id获取用户拥有的菜单,当upper为true时,获取的结果包含上级菜单,否则反之。<br>
     * 当combine为true时,将多级菜单进行组合,下级菜单会保存在{@link CustomerMenuVo#children}中,否则反之。<br>
     * 注意: combine为true时需要upper也为true才能生效。<br>
     * </p>
     *
     * @param userId  用户id
     * @param upper   是否包含上级菜单
     * @param combine 是否将多级菜单进行组合
     * @return 用户拥有的菜单
     */
    List<CustomerMenuVo> getCustomerMenusByUserId(@Nullable Long userId, boolean upper, boolean combine);
}

