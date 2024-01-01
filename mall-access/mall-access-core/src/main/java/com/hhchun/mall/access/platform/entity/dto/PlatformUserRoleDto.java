package com.hhchun.mall.access.platform.entity.dto;

import com.hhchun.mall.access.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.access.platform.constant.PlatformOtherConstant;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 平台用户关联角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class PlatformUserRoleDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 平台用户id
     */
    @NotNull(groups = ADD.class, message = "平台用户id不能为空")
    private Long userId;
    /**
     * 角色
     */
    private List<Role> roles;

    @Data
    public static class Role {
        /**
         * 角色id
         */
        private Long roleId;
        /**
         * 绑定{@link PlatformOtherConstant#TABLE_RELATION_ACTION_BOUND}
         * 解绑{@link PlatformOtherConstant#TABLE_RELATION_ACTION_UNBOUND
         */
        private Integer action;
    }

}
