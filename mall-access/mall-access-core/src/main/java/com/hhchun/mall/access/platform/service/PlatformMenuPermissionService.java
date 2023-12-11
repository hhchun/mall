package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuPermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformMenuPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformMenuPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;


/**
 * 平台菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface PlatformMenuPermissionService extends IService<PlatformMenuPermissionEntity> {

    void saveMenuPermissions(PlatformMenuPermissionDto menuPermissionDto);

    void removeMenuPermission(Long menuPermissionId);

    PageResult<PlatformPermissionVo> getPlatformBoundPermissionList(PlatformMenuPermissionSearchDto search);

    PageResult<PlatformPermissionVo> getPlatformUnboundPermissionList(PlatformMenuPermissionSearchDto search);
}

