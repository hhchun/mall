package com.hhchun.mall.user.customer.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerUserRoleDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserRoleSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerRoleVo;
import com.hhchun.mall.user.customer.service.CustomerUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 顾客用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/customer/user-role")
public class CustomerUserRoleController {
    @Autowired
    private CustomerUserRoleService customerUserRoleService;

    @PostMapping("/save")
    public R<?> saveCustomerUserRoles(@RequestBody @Validated(ADD.class) CustomerUserRoleDto userRoleDto) {
        customerUserRoleService.saveCustomerUserRoles(userRoleDto);

        return R.success();
    }

    @PostMapping("/roles")
    public R<PageResult<CustomerRoleVo>> getCustomerRoles(@RequestBody @Validated CustomerUserRoleSearchDto search) {
        PageResult<CustomerRoleVo> result = customerUserRoleService.getCustomerRoles(search);

        return R.success(result);
    }
}
