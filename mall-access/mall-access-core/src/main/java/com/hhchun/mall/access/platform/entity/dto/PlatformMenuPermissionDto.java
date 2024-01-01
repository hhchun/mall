package com.hhchun.mall.access.platform.entity.dto;

import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.platform.constant.PlatformOtherConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 平台菜单关联权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Data
public class PlatformMenuPermissionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单id
     */
    @NotNull(groups = ADD.class, message = "菜单id不能为空")
    private Long menuId;
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
