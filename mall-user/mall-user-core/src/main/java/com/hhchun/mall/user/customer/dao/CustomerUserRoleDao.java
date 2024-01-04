package com.hhchun.mall.user.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.customer.entity.bo.CustomerRoleBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerUserRoleEntity;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserRoleSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 顾客用户关联角色
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface CustomerUserRoleDao extends BaseMapper<CustomerUserRoleEntity> {

    IPage<CustomerRoleBo> getCustomerRoles(@Param("page") IPage<Object> page, @Param("search") CustomerUserRoleSearchDto search);

    List<Long> getCustomerRemovedUserIdsByRoleIds(@Param("roleIds") List<Long> roleIds);
}
