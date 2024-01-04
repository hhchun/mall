package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.user.customer.entity.domain.CustomerRolePermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerRolePermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRolePermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 顾客角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
public interface CustomerRolePermissionService extends IService<CustomerRolePermissionEntity> {

    void saveCustomerRolePermissions(CustomerRolePermissionDto rolePermissionDto);

    PageResult<CustomerPermissionVo> getCustomerPermissions(CustomerRolePermissionSearchDto search);

    List<Long> getCustomerRoleIdsByPermissionId(Long permissionId);

    /**
     * <p>
     * 根据权限id查询关联的所有角色id,包括已被删除(逻辑删除)的。
     * </p>
     *
     * @param permissionId 权限id
     * @return 角色id
     */
    @Nonnull
    List<Long> getCustomerRemovedRoleIdsByPermissionId(Long permissionId);

    @Nonnull
    List<Long> getCustomerPermissionIdsByRoleIds(Collection<Long> roleIds);
}

