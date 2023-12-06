package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformPermissionEntity;
import com.hhchun.mall.access.platform.entity.dto.PlatformPermissionDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformPermissionSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformPermissionVo;
import com.sun.istack.internal.NotNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 平台权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface PlatformPermissionService extends IService<PlatformPermissionEntity> {

    void savePlatformPermission(PlatformPermissionDto permissionDto);

    void modifyPlatformPermission(PlatformPermissionDto permissionDto);

    PlatformPermissionEntity getPlatformPermissionBySymbol(@NotNull String symbol);

    PlatformPermissionEntity getPlatformPermissionById(@NotNull Long id);

    void removePlatformPermission(Long permissionId);

    PageResult<PlatformPermissionVo> getPlatformPermissionList(PlatformPermissionSearchDto search);
}

