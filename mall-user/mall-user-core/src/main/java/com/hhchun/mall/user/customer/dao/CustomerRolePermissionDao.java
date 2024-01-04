package com.hhchun.mall.user.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.customer.entity.bo.CustomerPermissionBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerRolePermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRolePermissionSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 顾客角色关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface CustomerRolePermissionDao extends BaseMapper<CustomerRolePermissionEntity> {

    IPage<CustomerPermissionBo> getCustomerPermissions(@Param("page") IPage<Object> page, @Param("search") CustomerRolePermissionSearchDto search);

    List<Long> getCustomerRemovedRoleIdsByPermissionId(@Param("permissionId") Long permissionId);
}
