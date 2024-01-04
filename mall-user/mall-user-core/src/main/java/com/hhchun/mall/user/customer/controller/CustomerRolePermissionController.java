package com.hhchun.mall.user.customer.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerRolePermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRolePermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.hhchun.mall.user.customer.service.CustomerRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 顾客角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/customer/role-permission")
public class CustomerRolePermissionController {
    @Autowired
    private CustomerRolePermissionService customerRolePermissionService;

    @PostMapping("/save")
    public R<?> saveCustomerRolePermissions(@RequestBody @Validated(ADD.class) CustomerRolePermissionDto rolePermissionDto) {
        customerRolePermissionService.saveCustomerRolePermissions(rolePermissionDto);

        return R.success();
    }

    @PostMapping("/permissions")
    public R<PageResult<CustomerPermissionVo>> getCustomerPermissions(@RequestBody @Validated CustomerRolePermissionSearchDto search) {
        PageResult<CustomerPermissionVo> result = customerRolePermissionService.getCustomerPermissions(search);

        return R.success(result);
    }

}
