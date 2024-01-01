package com.hhchun.mall.access.platform.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.access.platform.entity.bo.PlatformPermissionBo;
import com.hhchun.mall.access.platform.entity.domain.PlatformMenuPermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformMenuPermissionSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 平台菜单关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Mapper
public interface PlatformMenuPermissionDao extends BaseMapper<PlatformMenuPermissionEntity> {

    IPage<PlatformPermissionBo> getPlatformPermissions(@Param("page") IPage<Object> page,
                                                       @Param("search") PlatformMenuPermissionSearchDto search);
}
