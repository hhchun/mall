package com.hhchun.mall.access.platform.entity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 平台菜单
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-08 22:58:31
 */
@Data
@TableName("ac_platform_menu")
public class PlatformMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 是否删除
	 */
	private Integer del;
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
	 * 层级/路径
	 */
	private String route;

}
