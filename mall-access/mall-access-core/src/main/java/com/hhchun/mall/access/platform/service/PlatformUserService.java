package com.hhchun.mall.access.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.platform.entity.domain.PlatformUserEntity;
import com.hhchun.mall.access.platform.entity.dto.LoginPlatformUserDto;
import com.hhchun.mall.access.platform.entity.dto.PlatformUserDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformUserSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformUserVo;
import com.sun.istack.internal.Nullable;

/**
 * 平台用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface PlatformUserService extends IService<PlatformUserEntity> {

    void savePlatformUser(PlatformUserDto userDto);

    void modifyPlatformUser(PlatformUserDto userDto);

    @Nullable
    PlatformUserEntity getPlatformUserByUsername(String username);

    PlatformUserEntity getPlatformUserById(Long id);

    PageResult<PlatformUserVo> getPlatformUserList(PlatformUserSearchDto search);

    String loginPlatformUser(LoginPlatformUserDto loginUserDto);
}

