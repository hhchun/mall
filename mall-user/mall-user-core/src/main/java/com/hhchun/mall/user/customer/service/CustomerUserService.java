package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.entity.domain.CustomerUserEntity;
import com.hhchun.mall.user.customer.entity.dto.LoginCustomerUserDto;
import com.hhchun.mall.user.customer.entity.dto.CustomerUserDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerUserVo;
import com.sun.istack.internal.Nullable;

/**
 * 顾客用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface CustomerUserService extends IService<CustomerUserEntity> {

    void saveCustomerUser(CustomerUserDto userDto);

    void modifyCustomerUser(CustomerUserDto userDto);

    @Nullable
    CustomerUserEntity getCustomerUserByUsername(String username);

    CustomerUserEntity getCustomerUserById(Long id);

    PageResult<CustomerUserVo> getCustomerUserList(CustomerUserSearchDto search);

    String loginCustomerUser(LoginCustomerUserDto loginUserDto);


    // 根据用户id获取用户拥有的所有菜单数据
}

