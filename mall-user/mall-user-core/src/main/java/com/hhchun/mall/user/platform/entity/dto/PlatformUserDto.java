package com.hhchun.mall.user.platform.entity.dto;

import com.hhchun.mall.common.constant.ValidationConstant.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 平台用户
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class PlatformUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(groups = UPDATE.class, message = "id不能为空")
    private Long id;
    /**
     * 用户名
     */
    @NotBlank(groups = ADD.class, message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(groups = ADD.class, message = "密码不能为空")
    private String password;
    /**
     * 昵称
     */
    @NotBlank(groups = ADD.class, message = "昵称不能为空")
    private String nickname;

}
