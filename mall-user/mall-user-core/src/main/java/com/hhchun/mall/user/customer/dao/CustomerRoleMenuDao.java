package com.hhchun.mall.user.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.user.customer.entity.bo.CustomerMenuBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleMenuEntity;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleMenuSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 顾客角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Mapper
public interface CustomerRoleMenuDao extends BaseMapper<CustomerRoleMenuEntity> {

    List<CustomerMenuBo> getCustomerMenuTree(@Param("search") CustomerRoleMenuSearchDto search);

    List<Long> getCustomerRemovedRoleIdsByMenuIds(@Param("menuIds") List<Long> menuIds);
}
