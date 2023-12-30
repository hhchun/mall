package com.hhchun.mall.access.platform.entity.dto;

import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

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
     * 权限id
     */
    @NotEmpty(groups = ADD.class, message = "没有选择权限")
    private Set<Long> permissionIds;

}
