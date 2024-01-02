package com.hhchun.mall.user.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.domain.PlatformRoleEntity;
import com.hhchun.mall.user.platform.entity.dto.PlatformRoleDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformRoleSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformRoleVo;
import com.sun.istack.internal.NotNull;

/**
 * 平台角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface PlatformRoleService extends IService<PlatformRoleEntity> {

    void savePlatformRole(PlatformRoleDto roleDto);

    PlatformRoleEntity getPlatformRoleBySymbol(@NotNull String symbol);

    void modifyPlatformRole(PlatformRoleDto roleDto);

    PlatformRoleEntity getPlatformRoleById(@NotNull Long id);

    void removePlatformRole(Long roleId);

    PageResult<PlatformRoleVo> getPlatformRoleList(PlatformRoleSearchDto search);
}

