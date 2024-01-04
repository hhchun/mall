package com.hhchun.mall.user.customer.controller;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerPermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerPermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.hhchun.mall.user.customer.service.CustomerPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 顾客权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/customer/permission")
public class CustomerPermissionController {
    @Autowired
    private CustomerPermissionService customerPermissionService;

    @PostMapping("/save")
    public R<?> saveCustomerPermission(@RequestBody @Validated(ADD.class) CustomerPermissionDto permissionDto) {
        customerPermissionService.saveCustomerPermission(permissionDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyCustomerPermission(@RequestBody @Validated(UPDATE.class) CustomerPermissionDto permissionDto) {
        customerPermissionService.modifyCustomerPermission(permissionDto);

        return R.success();
    }

    @PostMapping("/remove/{permissionId}")
    public R<?> removeCustomerPermission(@PathVariable Long permissionId) {
        customerPermissionService.removeCustomerPermission(permissionId);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<CustomerPermissionVo>> getCustomerPermissionList(@RequestBody CustomerPermissionSearchDto search) {
        PageResult<CustomerPermissionVo> result = customerPermissionService.getCustomerPermissionList(search);
        return R.success(result);
    }
}
