package com.hhchun.mall.user.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.domain.PlatformUserRoleEntity;
import com.hhchun.mall.user.platform.entity.dto.PlatformUserRoleDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformUserRoleSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformRoleVo;

import java.util.List;


/**
 * 平台用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface PlatformUserRoleService extends IService<PlatformUserRoleEntity> {

    void savePlatformUserRoles(PlatformUserRoleDto userRoleDto);

    PageResult<PlatformRoleVo> getPlatformRoles(PlatformUserRoleSearchDto search);

    List<Long> getPlatformUserIdsByRoleIds(List<Long> roleIds);

    List<Long> getPlatformRemovedUserIdsByRoleIds(List<Long> roleIds);

    List<Long> getPlatformRoleIdsByUserId(Long userId);
}

