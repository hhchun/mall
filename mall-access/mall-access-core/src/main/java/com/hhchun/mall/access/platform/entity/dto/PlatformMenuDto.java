package com.hhchun.mall.access.platform.entity.dto;

import com.hhchun.mall.access.common.constant.ValidationConstant.*;
import com.hhchun.mall.access.common.validation.ListValue;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 平台菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Data
public class PlatformMenuDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @NotNull(groups = UPDATE.class, message = "id不能为空")
    private Long id;
    /**
     * 菜单标识
     */
    @NotBlank(groups = ADD.class, message = "菜单标识不能为空")
    private String symbol;
    /**
     * 菜单名称
     */
    @NotBlank(groups = ADD.class, message = "菜单名称不能为空")
    private String name;
    /**
     * 描述/备注
     */
    private String des;
    /**
     * 父菜单id
     */
    @NotNull(groups = ADD.class, message = "父菜单必选")
    private Long pid;
    /**
     * 公开菜单,0-否、1-是
     */
    @ListValue(groups = {ADD.class, UPDATE.class}, values = {"0", "1"}, message = "overt can only be 0 and 1!")
    @NotNull(groups = ADD.class, message = "菜单是否公开必选")
    private Integer overt;
}
