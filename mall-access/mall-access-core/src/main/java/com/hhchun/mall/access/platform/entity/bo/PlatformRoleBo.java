package com.hhchun.mall.access.platform.entity.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 平台角色
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class PlatformRoleBo implements Serializable {
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
     * 角色标识
     */
    private String symbol;
    /**
     * 角色名
     */
    private String name;
    /**
     * 描述/备注
     */
    private String des;
    /**
     * 是否拥有,0-否、1-是
     */
    private Integer owned;
}
