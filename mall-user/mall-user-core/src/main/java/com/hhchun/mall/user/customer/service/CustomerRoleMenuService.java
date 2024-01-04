package com.hhchun.mall.user.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hhchun.mall.user.customer.entity.domain.CustomerRoleMenuEntity;
import com.hhchun.mall.user.customer.entity.dto.CustomerRoleMenuDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleMenuSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerMenuVo;

import java.util.Collection;
import java.util.List;

/**
 * 顾客角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
public interface CustomerRoleMenuService extends IService<CustomerRoleMenuEntity> {

    void saveCustomerRoleMenus(CustomerRoleMenuDto roleMenuDto);

    List<CustomerMenuVo> getCustomerMenuTree(CustomerRoleMenuSearchDto search);

    List<Long> getCustomerRoleIdsByMenuIds(List<Long> menuIds);

    List<Long> getCustomerRemovedRoleIdsByMenuIds(List<Long> menuIds);

    List<Long> getCustomerMenuIdsByRoleIds(Collection<Long> roleIds);
}

