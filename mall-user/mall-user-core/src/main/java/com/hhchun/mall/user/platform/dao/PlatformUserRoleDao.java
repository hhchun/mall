package com.hhchun.mall.user.platform.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.platform.entity.bo.PlatformRoleBo;
import com.hhchun.mall.user.platform.entity.domain.PlatformUserRoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformUserRoleSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台用户关联角色
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface PlatformUserRoleDao extends BaseMapper<PlatformUserRoleEntity> {

    IPage<PlatformRoleBo> getPlatformRoles(@Param("page") IPage<Object> page, @Param("search") PlatformUserRoleSearchDto search);

    List<Long> getPlatformRemovedUserIdsByRoleIds(@Param("roleIds") List<Long> roleIds);
}
