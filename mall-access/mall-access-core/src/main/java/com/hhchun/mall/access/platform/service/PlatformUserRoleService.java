package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformUserRoleEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformUserRoleDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformUserRoleSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformRoleVo;


/**
 * 平台用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface PlatformUserRoleService extends IService<PlatformUserRoleEntity> {

    void savePlatformUserRoles(PlatformUserRoleDto userRoleDto);

    void removePlatformUserRole(Long userRoleId);

    PageResult<PlatformRoleVo> getPlatformBoundRoleList(PlatformUserRoleSearchDto search);

    PageResult<PlatformRoleVo> getPlatformUnboundRoleList(PlatformUserRoleSearchDto search);
}

