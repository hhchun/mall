package com.hhchun.mall.access.platform.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.access.platform.entity.bo.PlatformPermissionBo;
import com.hhchun.mall.access.platform.entity.domain.PlatformRolePermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformRolePermissionSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台角色关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface PlatformRolePermissionDao extends BaseMapper<PlatformRolePermissionEntity> {

    IPage<PlatformPermissionBo> getPlatformPermissions(@Param("page") IPage<Object> page, @Param("search") PlatformRolePermissionSearchDto search);

    List<Long> getPlatformRemovedRoleIdsByPermissionId(@Param("permissionId") Long permissionId);
}
