package com.hhchun.mall.user.customer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hhchun.mall.user.customer.entity.bo.CustomerPermissionBo;
import com.hhchun.mall.user.customer.entity.domain.CustomerMenuPermissionEntity;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerMenuPermissionSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 顾客菜单关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Mapper
public interface CustomerMenuPermissionDao extends BaseMapper<CustomerMenuPermissionEntity> {

    IPage<CustomerPermissionBo> getCustomerPermissions(@Param("page") IPage<Object> page,
                                               @Param("search") CustomerMenuPermissionSearchDto search);
}
