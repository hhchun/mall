package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.entity.domain.CustomerPermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerPermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerPermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.sun.istack.internal.NotNull;

/**
 * 顾客权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface CustomerPermissionService extends IService<CustomerPermissionEntity> {

    void saveCustomerPermission(CustomerPermissionDto permissionDto);

    void modifyCustomerPermission(CustomerPermissionDto permissionDto);

    CustomerPermissionEntity getCustomerPermissionBySymbol(@NotNull String symbol);

    CustomerPermissionEntity getCustomerPermissionById(@NotNull Long id);

    void removeCustomerPermission(Long permissionId);

    PageResult<CustomerPermissionVo> getCustomerPermissionList(CustomerPermissionSearchDto search);
}

