package com.hhchun.mall.user.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.domain.PlatformMenuPermissionEntity;
import com.hhchun.mall.user.platform.entity.dto.PlatformMenuPermissionDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformMenuPermissionSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformPermissionVo;
import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.List;


/**
 * 平台菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface PlatformMenuPermissionService extends IService<PlatformMenuPermissionEntity> {

    void saveMenuPermissions(PlatformMenuPermissionDto menuPermissionDto);

    PageResult<PlatformPermissionVo> getPlatformPermissions(PlatformMenuPermissionSearchDto search);

    @NotNull
    List<Long> getPlatformPermissionIdsByMenuIds(Collection<Long> menuIds);
}

