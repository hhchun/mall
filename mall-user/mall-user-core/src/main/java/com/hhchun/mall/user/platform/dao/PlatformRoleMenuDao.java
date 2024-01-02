package com.hhchun.mall.user.platform.dao;

import com.hhchun.mall.user.platform.entity.bo.PlatformMenuBo;
import com.hhchun.mall.user.platform.entity.domain.PlatformRoleMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRoleMenuSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Mapper
public interface PlatformRoleMenuDao extends BaseMapper<PlatformRoleMenuEntity> {

    List<PlatformMenuBo> getPlatformMenuTree(@Param("search") PlatformRoleMenuSearchDto search);

    List<Long> getPlatformRemovedRoleIdsByMenuIds(@Param("menuIds") List<Long> menuIds);
}
