package com.hhchun.mall.access.platform.entity.dto;

import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

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
     * id
     */
    private Long id;
    /**
     * 平台用户id
     */
    @NotNull(groups = ADD.class, message = "平台用户id不能为空")
    private Long userId;
    /**
     * 角色id
     */
    @NotEmpty(groups = ADD.class, message = "没有选择角色")
    private Set<Long> roleIds;

}