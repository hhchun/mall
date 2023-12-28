package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformRolePermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformRolePermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;

import java.util.List;

/**
 * 平台角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface PlatformRolePermissionService extends IService<PlatformRolePermissionEntity> {

    void savePlatformRolePermissions(PlatformRolePermissionDto rolePermissionDto);

    void removePlatformRolePermission(Long platformRolePermissionId);

    PageResult<PlatformPermissionVo> getPlatformBoundPermissionList(PlatformRolePermissionSearchDto search);

    PageResult<PlatformPermissionVo> getPlatformUnboundPermissionList(PlatformRolePermissionSearchDto search);

    List<Long> getPlatformRoleIdsByPermissionId(Long permissionId);
}

