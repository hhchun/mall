package com.hhchun.mall.user.customer.entity.dto.search;

import com.hhchun.mall.common.utils.PageArguments;
import com.hhchun.mall.common.validation.ListValue;
import lombok.Data;

import java.io.Serializable;

/**
 * 顾客权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class CustomerPermissionSearchDto extends PageArguments implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 权限标识
     */
    private String symbol;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限主体
     */
    private String subject;
    /**
     * 权限是否公开,0-否、1-是
     */
    @ListValue(values = {"0", "1"}, message = "overt can only be 0 and 1!")
    private Integer overt;

}
