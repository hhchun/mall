package com.hhchun.mall.user.shop.entity.bo;

import com.hhchun.mall.user.shop.constant.ShopMenuConstant;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺菜单
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Data
public class ShopMenuBo implements Serializable {
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
     * @see ShopMenuConstant#MENU_TYPE_MENU
     * @see ShopMenuConstant#MENU_TYPE_OPERATE
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
