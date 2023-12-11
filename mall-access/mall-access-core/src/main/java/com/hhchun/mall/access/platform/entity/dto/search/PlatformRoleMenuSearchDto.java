package com.hhchun.mall.access.platform.entity.dto.search;

import com.hhchun.mall.access.common.utils.PageArguments;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlatformRoleMenuSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
}
