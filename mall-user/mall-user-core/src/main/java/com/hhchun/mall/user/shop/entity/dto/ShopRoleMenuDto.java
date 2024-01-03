package com.hhchun.mall.user.shop.entity.dto;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.user.shop.constant.ShopOtherConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 店铺角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Data
public class ShopRoleMenuDto implements Serializable {
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
         * 绑定{@link ShopOtherConstant#TABLE_RELATION_ACTION_BOUND}
         * 解绑{@link ShopOtherConstant#TABLE_RELATION_ACTION_UNBOUND
         */
        private Integer action;
    }

}
