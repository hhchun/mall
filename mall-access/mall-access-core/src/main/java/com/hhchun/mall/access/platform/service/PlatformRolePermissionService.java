package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformRolePermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformRolePermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;

import javax.annotation.Nonnull;
import java.util.Collection;
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

    PageResult<PlatformPermissionVo> getPlatformPermissions(PlatformRolePermissionSearchDto search);

    List<Long> getPlatformRoleIdsByPermissionId(Long permissionId);

    /**
     * <p>
     * 根据权限id查询关联的所有角色id,包括已被删除(逻辑删除)的。
     * </p>
     *
     * @param permissionId 权限id
     * @return 角色id
     */
    @Nonnull
    List<Long> getPlatformRemovedRoleIdsByPermissionId(Long permissionId);

    @Nonnull
    List<Long> getPlatformPermissionIdsByRoleIds(Collection<Long> roleIds);
}

