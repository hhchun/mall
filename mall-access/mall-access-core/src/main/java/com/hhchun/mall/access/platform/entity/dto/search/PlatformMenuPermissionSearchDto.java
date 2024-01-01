package com.hhchun.mall.access.platform.entity.dto.search;

import com.hhchun.mall.access.common.utils.PageArguments;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PlatformMenuPermissionSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单id
     */
    @NotNull(message = "menuId require")
    private Long menuId;
    /**
     * 是否拥有,0-否、1-是
     */
    private Integer owned;
}
