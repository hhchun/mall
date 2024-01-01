package com.hhchun.mall.access.platform.entity.bo;

import com.hhchun.mall.access.platform.constant.PlatformMenuConstant;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 平台菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Data
public class PlatformMenuBo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 菜单标识
     */
    private String symbol;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 类型
     *
     * @see PlatformMenuConstant#MENU_TYPE_MENU
     * @see PlatformMenuConstant#MENU_TYPE_OPERATE
     */
    private Integer type;
    /**
     * 描述/备注
     */
    private String des;
    /**
     * 父菜单id
     */
    private Long pid;
    /**
     * 公开的菜单,0-否、1-是
     */
    private Integer overt;
    /**
     * 是否拥有,0-否、1-是
     */
    private Integer owned;
}
