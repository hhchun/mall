package com.hhchun.mall.user.customer.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 顾客权限
 *
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class CustomerPermissionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /**
     * 权限标识
     */
    private String symbol;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 描述/备注
     */
    private String des;
    /**
     * 权限主体
     */
    private String subject;
    /**
     * 公开权限,0-否、1-是
     */
    private Integer overt;
    /**
     * 是否拥有,0-否、1-是
     */
    private Integer owned;

}
