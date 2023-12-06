package com.hhchun.mall.access.platform.entity.dto.search;

import com.hhchun.mall.access.common.utils.PageArguments;
import lombok.Data;

import java.io.Serializable;

/**
 * 平台角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class PlatformRoleSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 角色标识
     */
    private String symbol;
    /**
     * 角色名
     */
    private String name;
}
