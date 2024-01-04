package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerRoleDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerRoleVo;
import com.sun.istack.internal.NotNull;

/**
 * 顾客角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface CustomerRoleService extends IService<CustomerRoleEntity> {

    void saveCustomerRole(CustomerRoleDto roleDto);

    CustomerRoleEntity getCustomerRoleBySymbol(@NotNull String symbol);

    void modifyCustomerRole(CustomerRoleDto roleDto);

    CustomerRoleEntity getCustomerRoleById(@NotNull Long id);

    void removeCustomerRole(Long roleId);

    PageResult<CustomerRoleVo> getCustomerRoleList(CustomerRoleSearchDto search);
}

