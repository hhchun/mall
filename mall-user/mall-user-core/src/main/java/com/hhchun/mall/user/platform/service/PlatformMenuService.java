package com.hhchun.mall.user.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.user.platform.entity.domain.PlatformMenuEntity;
import com.hhchun.mall.user.platform.entity.dto.PlatformMenuDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.List;


/**
 * 平台菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */

public interface PlatformMenuService extends IService<PlatformMenuEntity> {

    List<PlatformMenuVo> getPlatformMenuTree(@Nullable Long pid);

    @Nullable
    PlatformMenuEntity getPlatformMenuBySymbol(String symbol);

    @Nullable
    PlatformMenuEntity getPlatformMenuById(Long id);

    void savePlatformMenu(PlatformMenuDto menuDto);

    void modifyPlatformMenu(PlatformMenuDto menuDto);

    void removePlatformMenu(Long menuId);

    List<Long> getPlatformLowerMenuIdsByRoute(String route);

    @NotNull
    List<Long> getPlatformUpperMenuIdsByMenuIds(Collection<Long> menuIds);

    /**
     * <p>
     * 根据用户id获取用户拥有的菜单,当upper为true时,获取的结果包含上级菜单,否则反之。<br>
     * 当combine为true时,将多级菜单进行组合,下级菜单会保存在{@link PlatformMenuVo#children}中,否则反之。<br>
     * 注意: combine为true时需要upper也为true才能生效。<br>
     * </p>
     *
     * @param userId  用户id
     * @param upper   是否包含上级菜单
     * @param combine 是否将多级菜单进行组合
     * @return 用户拥有的菜单
     */
    List<PlatformMenuVo> getPlatformMenusByUserId(@Nullable Long userId, boolean upper, boolean combine);
}

