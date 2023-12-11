package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformRoleMenuEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformRoleMenuDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformMenuVo;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;

/**
 * 平台角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface PlatformRoleMenuService extends IService<PlatformRoleMenuEntity> {

    void savePlatformRoleMenus(PlatformRoleMenuDto roleMenuDto);

    void removePlatformRoleMenu(Long roleMenuId);

    PageResult<PlatformMenuVo> getPlatformBoundMenuList(PlatformRoleMenuSearchDto search);

    PageResult<PlatformMenuVo> getPlatformUnboundMenuList(PlatformRoleMenuSearchDto search);

    PageResult<PlatformPermissionVo> getPlatformBoundPermissionList(PlatformRolePermissionSearchDto search);
}

