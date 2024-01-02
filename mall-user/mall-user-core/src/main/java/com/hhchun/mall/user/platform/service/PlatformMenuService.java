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
}

