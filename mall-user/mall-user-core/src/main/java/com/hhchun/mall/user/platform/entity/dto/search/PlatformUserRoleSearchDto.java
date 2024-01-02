package com.hhchun.mall.user.platform.entity.dto.search;

import com.hhchun.mall.common.utils.PageArguments;
import com.hhchun.mall.common.validation.ListValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PlatformUserRoleSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @NotNull(message = "userId require")
    private Long userId;

    /**
     * 是否拥有,0-否、1-是
     */
    @ListValue(values = {"0", "1"}, message = "owned values can only be 0 or 1")
    private Integer owned;
}
