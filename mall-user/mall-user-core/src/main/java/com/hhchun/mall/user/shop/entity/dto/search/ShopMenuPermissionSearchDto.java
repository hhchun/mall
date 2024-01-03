package com.hhchun.mall.user.shop.entity.dto.search;

import com.hhchun.mall.common.utils.PageArguments;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ShopMenuPermissionSearchDto extends PageArguments implements Serializable {
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
