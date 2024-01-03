package com.hhchun.mall.user.shop.entity.dto.search;

import com.hhchun.mall.common.utils.PageArguments;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class ShopRoleSearchDto extends PageArguments implements Serializable {
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
