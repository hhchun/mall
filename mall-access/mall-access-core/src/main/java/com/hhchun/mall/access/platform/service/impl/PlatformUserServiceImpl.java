package com.hhchun.mall.access.platform.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.common.utils.PageResult;
import com.hhchun.mall.access.common.utils.TokenUtils;
import com.hhchun.mall.access.platform.dao.PlatformUserDao;
import com.hhchun.mall.access.platform.entity.dto.LoginPlatformUserDto;
import com.hhchun.mall.access.platform.entity.dto.PlatformUserDto;
import com.hhchun.mall.access.platform.entity.dto.search.PlatformUserSearchDto;
import com.hhchun.mall.access.platform.entity.vo.PlatformUserVo;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.access.platform.entity.domain.PlatformUserEntity;
import com.hhchun.mall.access.platform.service.PlatformUserService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("platformUserService")
public class PlatformUserServiceImpl extends ServiceImpl<PlatformUserDao, PlatformUserEntity> implements PlatformUserService {

    @Override
    public void savePlatformUser(PlatformUserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String nickname = userDto.getNickname();
        PlatformUserEntity platformUser = getPlatformUserByUsername(username);
        Preconditions.checkCondition(platformUser == null, "用户名已存在");
        platformUser = new PlatformUserEntity();
        platformUser.setUsername(username);
        platformUser.setPassword(SecureUtil.md5(password));
        platformUser.setNickname(nickname);
        save(platformUser);
    }

    @Override
    public void modifyPlatformUser(PlatformUserDto userDto) {
        Long id = userDto.getId();
        String nickname = userDto.getNickname();
        String password = userDto.getPassword();
        PlatformUserEntity platformUser = getPlatformUserById(id);
        Preconditions.checkCondition(platformUser != null, "用户不存在");

        if (!StringUtils.hasLength(password) && !StringUtils.hasLength(nickname)) {
            // 没有什么要修改的
            return;
        }
        if (StringUtils.hasLength(nickname)) {
            platformUser.setNickname(nickname);
        }
        if (StringUtils.hasLength(password)) {
            platformUser.setPassword(SecureUtil.md5(password));
        }
        updateById(platformUser);
    }

    @Override
    @Nullable
    public PlatformUserEntity getPlatformUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<PlatformUserEntity>()
                .eq(PlatformUserEntity::getUsername, username));
    }

    @Override
    @Nullable
    public PlatformUserEntity getPlatformUserById(Long id) {
        return getById(id);
    }

    @Override
    public PageResult<PlatformUserVo> getPlatformUserList(PlatformUserSearchDto search) {
        String username = search.getUsername();
        String nickname = search.getNickname();
        LambdaQueryWrapper<PlatformUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlatformUserEntity::getUsername, username);
        wrapper.like(StringUtils.hasLength(nickname), PlatformUserEntity::getNickname, nickname);
        IPage<PlatformUserEntity> page = page(search.getPage(), wrapper);
        List<PlatformUserEntity> users = page.getRecords();
        List<PlatformUserVo> userVos = users.stream().map(user -> {
            PlatformUserVo userVo = new PlatformUserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, userVos);
    }

    @Override
    public String loginPlatformUser(LoginPlatformUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        PlatformUserEntity platformUser = getPlatformUserByUsername(username);
        Preconditions.checkCondition(platformUser != null, "用户名或密码错误");
        Preconditions.checkCondition(SecureUtil.md5(password).equals(platformUser.getPassword()), "用户名或密码错误");
        Long userId = platformUser.getId();
        return TokenUtils.getToken(userId);
    }
}