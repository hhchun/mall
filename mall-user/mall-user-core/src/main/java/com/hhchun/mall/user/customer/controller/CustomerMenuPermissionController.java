package com.hhchun.mall.user.customer.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerMenuPermissionDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerMenuPermissionSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerPermissionVo;
import com.hhchun.mall.user.customer.service.CustomerMenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 顾客菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@RestController
@RequestMapping("/user/customer/menu-permission")
public class CustomerMenuPermissionController {
    @Autowired
    private CustomerMenuPermissionService customerMenuPermissionService;

    @PostMapping("/save")
    public R<?> saveMenuPermissions(@RequestBody @Validated(ADD.class) CustomerMenuPermissionDto menuPermissionDto) {
        customerMenuPermissionService.saveMenuPermissions(menuPermissionDto);

        return R.success();
    }

    @PostMapping("/permissions")
    public R<PageResult<CustomerPermissionVo>> getCustomerPermissions(@RequestBody @Validated CustomerMenuPermissionSearchDto search) {
        PageResult<CustomerPermissionVo> result = customerMenuPermissionService.getCustomerPermissions(search);

        return R.success(result);
    }

}
