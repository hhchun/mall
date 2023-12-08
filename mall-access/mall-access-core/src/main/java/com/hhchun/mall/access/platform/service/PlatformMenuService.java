package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformMenuDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformMenuVo;
import com.sun.istack.internal.Nullable;

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

    void savePlatformMenu(PlatformMenuDto menuDto);
    @Nullable
    PlatformMenuEntity getPlatformMenuBySymbol(String symbol);

    void modifyPlatformMenu(PlatformMenuDto menuDto);

    void removePlatformMenu(Long menuId);
}

