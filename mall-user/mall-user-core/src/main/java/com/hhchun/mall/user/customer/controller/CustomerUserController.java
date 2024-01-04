package com.hhchun.mall.user.customer.controller;


import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.PageResult;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.LoginCustomerUserDto;
import com.hhchun.mall.user.customer.entity.dto.CustomerUserDto;
import com.hhchun.mall.user.customer.entity.dto.search.CustomerUserSearchDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerUserVo;
import com.hhchun.mall.user.customer.service.CustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 顾客用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@RestController
@RequestMapping("/user/customer/user")
public class CustomerUserController {
    @Autowired
    private CustomerUserService customerUserService;

    @PostMapping("/save")
    public R<?> saveCustomerUser(@RequestBody @Validated(ADD.class) CustomerUserDto userDto) {
        customerUserService.saveCustomerUser(userDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyCustomerUser(@RequestBody @Validated(UPDATE.class) CustomerUserDto userDto) {
        customerUserService.modifyCustomerUser(userDto);

        return R.success();
    }

    @PostMapping("/list")
    public R<PageResult<CustomerUserVo>> getCustomerUserList(@RequestBody CustomerUserSearchDto search) {
        PageResult<CustomerUserVo> result = customerUserService.getCustomerUserList(search);

        return R.success(result);
    }

    @PostMapping("/login")
    public R<String> loginCustomerUser(@RequestBody @Validated LoginCustomerUserDto loginUserDto) {
        String token = customerUserService.loginCustomerUser(loginUserDto);

        return R.success(token);
    }
}
