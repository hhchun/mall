package com.hhchun.mall.user.shop.entity.dto;

import com.hhchun.mall.common.constant.ValidationConstant.ADD;
import com.hhchun.mall.common.constant.ValidationConstant.UPDATE;
import com.hhchun.mall.common.validation.ListValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class ShopPermissionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(groups = UPDATE.class, message = "权限id不能为空")
    private Long id;
    /**
     * 权限标识
     */
    @NotBlank(groups = ADD.class, message = "权限标识不能为空")
    private String symbol;
    /**
     * 权限名称
     */
    @NotBlank(groups = ADD.class, message = "权限名称不能为空")
    private String name;
    /**
     * 描述/备注
     */
    private String des;
    /**
     * 权限主体
     */
    @NotBlank(groups = ADD.class, message = "权限主体不能为空")
    private String subject;
    /**
     * 权限是否公开,0-否、1-是
     */
    @ListValue(groups = {ADD.class, UPDATE.class}, values = {"0", "1"}, message = "overt can only be 0 and 1!")
    @NotNull(groups = ADD.class, message = "权限是否公开必选")
    private Integer overt;

}
