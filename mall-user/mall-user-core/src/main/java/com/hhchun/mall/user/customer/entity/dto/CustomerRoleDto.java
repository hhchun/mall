package com.hhchun.mall.user.customer.entity.dto;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 顾客角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class CustomerRoleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(groups = UPDATE.class, message = "角色id不能为空")
    private Long id;
    /**
     * 角色标识
     */
    @NotBlank(groups = ADD.class, message = "角色标识不能为空")
    private String symbol;
    /**
     * 角色名称
     */
    @NotBlank(groups = ADD.class, message = "角色名称不能为空")
    private String name;
    /**
     * 描述/备注
     */
    private String des;

}
