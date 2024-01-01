package com.hhchun.mall.access.platform.entity.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.hhchun.mall.access.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.access.platform.constant.PlatformOtherConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 平台角色关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
@TableName("ac_platform_role_permission")
public class PlatformRolePermissionDto implements Serializable {
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
         * 绑定{@link PlatformOtherConstant#TABLE_RELATION_ACTION_BOUND}
         * 解绑{@link PlatformOtherConstant#TABLE_RELATION_ACTION_UNBOUND
         */
        private Integer action;
    }

}
