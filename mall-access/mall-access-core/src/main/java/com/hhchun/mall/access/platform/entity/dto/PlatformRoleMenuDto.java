package com.hhchun.mall.access.platform.entity.dto;

import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * 平台角色关联菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Data
public class PlatformRoleMenuDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    @NotNull(groups = ADD.class, message = "角色id不能为空")
    private Long roleId;
    /**
     * 菜单id
     */
    @NotEmpty(groups = ADD.class, message = "没有选择菜单")
    private Set<Long> menuIds;

}
