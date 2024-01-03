package com.hhchun.mall.user.shop.entity.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.user.shop.constant.ShopOtherConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 店铺角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class ShopRolePermissionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    @NotNull(groups = ADD.class, message = "角色id不能为空")
    private Long roleId;
    /**
     * 权限
     */
    private List<Permission> permissions;

    @Data
    public static class Permission {
        /**
         * 权限id
         */
        private Long permissionId;
        /**
         * 绑定{@link ShopOtherConstant#TABLE_RELATION_ACTION_BOUND}
         * 解绑{@link ShopOtherConstant#TABLE_RELATION_ACTION_UNBOUND
         */
        private Integer action;
    }

}
