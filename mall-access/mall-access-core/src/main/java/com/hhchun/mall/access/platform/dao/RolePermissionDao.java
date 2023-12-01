package com.hhchun.mall.access.platform.dao;

import com.hhchun.mall.access.platform.entity.domain.RolePermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台角色关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface RolePermissionDao extends BaseMapper<RolePermissionEntity> {
	
}
