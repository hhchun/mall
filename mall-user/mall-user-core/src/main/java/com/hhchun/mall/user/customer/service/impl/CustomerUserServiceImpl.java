package com.hhchun.mall.user.customer.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.TokenUtils;
import com.hhchun.mall.user.customer.dao.CustomerUserDao;
import com.hhchun.mall.user.customer.entity.domain.CustomerUserEntity;
import com.hhchun.mall.user.customer.entity.dto.LoginCustomerUserDto;
import com.hhchun.mall.user.customer.entity.dto.CustomerUserDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerUserVo;
import com.hhchun.mall.user.customer.service.CustomerUserService;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("customerUserService")
public class CustomerUserServiceImpl extends ServiceImpl<CustomerUserDao, CustomerUserEntity> implements CustomerUserService {

    @Override
    public void saveCustomerUser(CustomerUserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        String nickname = userDto.getNickname();
        CustomerUserEntity customerUser = getCustomerUserByUsername(username);
        Preconditions.checkCondition(customerUser == null, "用户名已存在");
        customerUser = new CustomerUserEntity();
        customerUser.setUsername(username);
        customerUser.setPassword(SecureUtil.md5(password));
        customerUser.setNickname(nickname);
        save(customerUser);
    }

    @Override
    public void modifyCustomerUser(CustomerUserDto userDto) {
        Long id = userDto.getId();
        String nickname = userDto.getNickname();
        String password = userDto.getPassword();
        CustomerUserEntity customerUser = getCustomerUserById(id);
        Preconditions.checkCondition(customerUser != null, "用户不存在");

        if (!StringUtils.hasLength(password) && !StringUtils.hasLength(nickname)) {
            // 没有什么要修改的
            return;
        }
        if (StringUtils.hasLength(nickname)) {
            customerUser.setNickname(nickname);
        }
        if (StringUtils.hasLength(password)) {
            customerUser.setPassword(SecureUtil.md5(password));
        }
        updateById(customerUser);
    }

    @Override
    @Nullable
    public CustomerUserEntity getCustomerUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<CustomerUserEntity>()
                .eq(CustomerUserEntity::getUsername, username));
    }

    @Override
    @Nullable
    public CustomerUserEntity getCustomerUserById(Long id) {
        return getById(id);
    }

    @Override
    public PageResult<CustomerUserVo> getCustomerUserList(CustomerUserSearchDto search) {
        String username = search.getUsername();
        String nickname = search.getNickname();
        LambdaQueryWrapper<CustomerUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerUserEntity::getUsername, username);
        wrapper.like(StringUtils.hasLength(nickname), CustomerUserEntity::getNickname, nickname);
        IPage<CustomerUserEntity> page = page(search.getPage(), wrapper);
        List<CustomerUserEntity> users = page.getRecords();
        List<CustomerUserVo> userVos = users.stream().map(user -> {
            CustomerUserVo userVo = new CustomerUserVo();
            BeanUtils.copyProperties(user, userVo);
            return userVo;
        }).collect(Collectors.toList());
        return PageResult.convert(page, userVos);
    }

    @Override
    public String loginCustomerUser(LoginCustomerUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        CustomerUserEntity customerUser = getCustomerUserByUsername(username);
        Preconditions.checkCondition(customerUser != null, "用户名或密码错误");
        Preconditions.checkCondition(SecureUtil.md5(password).equals(customerUser.getPassword()), "用户名或密码错误");
        Long userId = customerUser.getId();
        return TokenUtils.getToken(userId);
    }
}