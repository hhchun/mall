package com.hhchun.mall.user.customer.controller;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.utils.R;
import com.hhchun.mall.user.customer.entity.dto.CustomerMenuDto;
import com.hhchun.mall.user.customer.entity.vo.CustomerMenuVo;
import com.hhchun.mall.user.customer.service.CustomerMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 顾客菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@RestController
@RequestMapping("/user/customer/menu")
public class CustomerMenuController {
    @Autowired
    private CustomerMenuService customerMenuService;

    @PostMapping("/tree/{pid}")
    public R<List<CustomerMenuVo>> getCustomerMenuTree(@PathVariable(required = false) Long pid) {
        List<CustomerMenuVo> menuVos = customerMenuService.getCustomerMenuTree(pid);
        return R.success(menuVos);
    }

    @PostMapping("/save")
    public R<?> saveCustomerMenu(@RequestBody @Validated(ADD.class) CustomerMenuDto menuDto) {
        customerMenuService.saveCustomerMenu(menuDto);

        return R.success();
    }

    @PostMapping("/modify")
    public R<?> modifyCustomerMenu(@RequestBody @Validated(UPDATE.class) CustomerMenuDto menuDto) {
        customerMenuService.modifyCustomerMenu(menuDto);

        return R.success();
    }

    @PostMapping("/remove/{menuId}")
    public R<?> removeCustomerMenu(@PathVariable Long menuId) {
        customerMenuService.removeCustomerMenu(menuId);

        return R.success();
    }

}
