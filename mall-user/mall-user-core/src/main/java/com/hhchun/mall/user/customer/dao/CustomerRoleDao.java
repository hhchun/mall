package com.hhchun.mall.user.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 顾客角色
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Mapper
public interface CustomerRoleDao extends BaseMapper<CustomerRoleEntity> {
	
}
