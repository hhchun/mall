package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuPermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerMenuPermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerMenuPermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.List;


/**
 * 顾客菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface CustomerMenuPermissionService extends IService<CustomerMenuPermissionEntity> {

    void saveMenuPermissions(CustomerMenuPermissionDto menuPermissionDto);

    PageResult<CustomerPermissionVo> getCustomerPermissions(CustomerMenuPermissionSearchDto search);

    @NotNull
    List<Long> getCustomerPermissionIdsByMenuIds(Collection<Long> menuIds);
}

