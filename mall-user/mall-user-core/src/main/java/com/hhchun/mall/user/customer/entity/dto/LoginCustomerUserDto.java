package com.hhchun.mall.user.customer.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginCustomerUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
