package com.hhchun.mall.user.customer.entity.dto.search;

import com.hhchun.mall.common.utils.PageArguments;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CustomerRoleMenuSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    @NotNull(message = "roleId require")
    private Long roleId;
    /**
     * 父菜单id
     */
    private Long menuPid = 0L;
    /**
     * 是否拥有,0-否、1-是
     */
    private Integer owned;
}
