package com.hhchun.mall.user.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.platform.entity.domain.PlatformUserEntity;
import com.hhchun.mall.user.platform.entity.dto.LoginPlatformUserDto;
import com.hhchun.mall.user.platform.entity.dto.PlatformUserDto;
import com.hhchun.mall.user.platform.entity.dto.search.PlatformUserSearchDto;
import com.hhchun.mall.user.platform.entity.vo.PlatformUserVo;
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


    // 根据用户id获取用户拥有的所有菜单数据
}

