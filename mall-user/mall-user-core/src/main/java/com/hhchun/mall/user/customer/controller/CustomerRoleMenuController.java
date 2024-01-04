package com.hhchun.mall.user.customer.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerRoleMenuDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleMenuSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerMenuVo;
import com.hhchun.mall.user.customer.service.CustomerRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 顾客角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@RestController
@RequestMapping("/user/customer/role-menu")
public class CustomerRoleMenuController {
    @Autowired
    private CustomerRoleMenuService customerRoleMenuService;

    @PostMapping("/save")
    public R<?> saveCustomerRoleMenus(@RequestBody @Validated(ADD.class) CustomerRoleMenuDto roleMenuDto) {
        customerRoleMenuService.saveCustomerRoleMenus(roleMenuDto);

        return R.success();
    }

    @PostMapping("/menu-tree")
    public R<List<CustomerMenuVo>> getCustomerMenuTree(@RequestBody @Validated CustomerRoleMenuSearchDto search) {
        List<CustomerMenuVo> result = customerRoleMenuService.getCustomerMenuTree(search);

        return R.success(result);
    }
}
