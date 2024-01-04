package com.hhchun.mall.user.customer.controller;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerRoleDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerRoleSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerRoleVo;
import com.hhchun.mall.user.customer.service.CustomerRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 顾客角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/customer/role")
public class CustomerRoleController {
    @Autowired
    private CustomerRoleService customerRoleService;

    @PostMapping("/save")
    public R<?> saveCustomerRole(@RequestBody @Validated(ADD.class) CustomerRoleDto roleDto) {
        customerRoleService.saveCustomerRole(roleDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyCustomerRole(@RequestBody @Validated(UPDATE.class) CustomerRoleDto roleDto) {
        customerRoleService.modifyCustomerRole(roleDto);

        return R.success();
    }

    @PostMapping("/remove/{roleId}")
    public R<?> removeCustomerRole(@PathVariable Long roleId) {
        customerRoleService.removeCustomerRole(roleId);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<CustomerRoleVo>> getCustomerRoleList(@RequestBody CustomerRoleSearchDto search) {
        PageResult<CustomerRoleVo> result = customerRoleService.getCustomerRoleList(search);
        return R.success(result);
    }

}
