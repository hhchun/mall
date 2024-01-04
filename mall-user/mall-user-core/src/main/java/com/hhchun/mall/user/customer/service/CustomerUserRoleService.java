package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.entity.domain.CustomerUserRoleEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerUserRoleDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserRoleSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerRoleVo;

import java.util.List;


/**
 * 顾客用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface CustomerUserRoleService extends IService<CustomerUserRoleEntity> {

    void saveCustomerUserRoles(CustomerUserRoleDto userRoleDto);

    PageResult<CustomerRoleVo> getCustomerRoles(CustomerUserRoleSearchDto search);

    List<Long> getCustomerUserIdsByRoleIds(List<Long> roleIds);

    List<Long> getCustomerRemovedUserIdsByRoleIds(List<Long> roleIds);

    List<Long> getCustomerRoleIdsByUserId(Long userId);
}

