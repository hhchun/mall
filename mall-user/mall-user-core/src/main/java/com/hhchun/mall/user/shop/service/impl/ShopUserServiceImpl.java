package com.hhchun.mall.user.shop.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.TokenUtils;
import com.hhchun.mall.user.shop.dao.ShopUserDao;
import com.hhchun.mall.user.shop.entity.domain.ShopUserEntity;
import com.hhchun.mall.user.shop.entity.dto.LoginShopUserDto;
import com.hhchun.mall.user.shop.entity.dto.ShopUserDto;
import com.hhchun.mall.user.shop.entity.dto.search.ShopUserSearchDto;
import com.hhchun.mall.user.shop.entity.vo.ShopUserVo;
import com.hhchun.mall.user.shop.service.ShopUserService;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("shopUserService")
public class ShopUserServiceImpl extends ServiceImpl<ShopUserDao, ShopUserEntity> implements ShopUserService {

    @Override
    public void saveShopUser(ShopUserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String nickname = userDto.getNickname();
        ShopUserEntity shopUser = getShopUserByUsername(username);
        Preconditions.checkCondition(shopUser == null, "用户名已存在");
        shopUser = new ShopUserEntity();
        shopUser.setUsername(username);
        shopUser.setPassword(SecureUtil.md5(password));
        shopUser.setNickname(nickname);
        save(shopUser);
    }

    @Override
    public void modifyShopUser(ShopUserDto userDto) {
        Long id = userDto.getId();
        String nickname = userDto.getNickname();
        String password = userDto.getPassword();
        ShopUserEntity shopUser = getShopUserById(id);
        Preconditions.checkCondition(shopUser != null, "用户不存在");

        if (!StringUtils.hasLength(password) && !StringUtils.hasLength(nickname)) {
            // 没有什么要修改的
            return;
        }
        if (StringUtils.hasLength(nickname)) {
            shopUser.setNickname(nickname);
        }
        if (StringUtils.hasLength(password)) {
            shopUser.setPassword(SecureUtil.md5(password));
        }
        updateById(shopUser);
    }

    @Override
    @Nullable
    public ShopUserEntity getShopUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<ShopUserEntity>()
                .eq(ShopUserEntity::getUsername, username));
    }

    @Override
    @Nullable
    public ShopUserEntity getShopUserById(Long id) {
        return getById(id);
    }

    @Override
    public PageResult<ShopUserVo> getShopUserList(ShopUserSearchDto search) {
        String username = search.getUsername();
        String nickname = search.getNickname();
        LambdaQueryWrapper<ShopUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopUserEntity::getUsername, username);
        wrapper.like(StringUtils.hasLength(nickname), ShopUserEntity::getNickname, nickname);
        IPage<ShopUserEntity> page = page(search.getPage(), wrapper);
        List<ShopUserEntity> users = page.getRecords();
        List<ShopUserVo> userVos = users.stream().map(user -> {
            ShopUserVo userVo = new ShopUserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, userVos);
    }

    @Override
    public String loginShopUser(LoginShopUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        ShopUserEntity shopUser = getShopUserByUsername(username);
        Preconditions.checkCondition(shopUser != null, "用户名或密码错误");
        Preconditions.checkCondition(SecureUtil.md5(password).equals(shopUser.getPassword()), "用户名或密码错误");
        Long userId = shopUser.getId();
        return TokenUtils.getToken(userId);
    }
}