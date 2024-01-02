package com.hhchun.mall.user.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.user.platform.entity.domain.PlatformRoleMenuEntity;
import com.hhchun.mall.user.platform.entity.dto.PlatformRoleMenuDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;

import java.util.Collection;
import java.util.List;

/**
 * 平台角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface PlatformRoleMenuService extends IService<PlatformRoleMenuEntity> {

    void savePlatformRoleMenus(PlatformRoleMenuDto roleMenuDto);

    List<PlatformMenuVo> getPlatformMenuTree(PlatformRoleMenuSearchDto search);
    List<Long> getPlatformRoleIdsByMenuIds(List<Long> menuIds);

    List<Long> getPlatformRemovedRoleIdsByMenuIds(List<Long> menuIds);

    List<Long> getPlatformMenuIdsByRoleIds(Collection<Long> roleIds);
}

