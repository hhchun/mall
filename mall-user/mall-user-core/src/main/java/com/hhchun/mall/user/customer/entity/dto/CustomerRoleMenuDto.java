package com.hhchun.mall.user.customer.entity.dto;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.user.customer.constant.CustomerOtherConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 顾客角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Data
public class CustomerRoleMenuDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    @NotNull(groups = ADD.class, message = "角色id不能为空")
    private Long roleId;
    /**
     * 菜单
     */
    private List<Menu> menus;

    @Data
    public static class Menu {
        /**
         * 菜单id
         */
        private Long menuId;
        /**
         * 绑定{@link CustomerOtherConstant#TABLE_RELATION_ACTION_BOUND}
         * 解绑{@link CustomerOtherConstant#TABLE_RELATION_ACTION_UNBOUND
         */
        private Integer action;
    }

}
