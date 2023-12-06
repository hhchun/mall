package com.hhchun.mall.access.platform.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 平台用户关联角色
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-01 23:20:10
 */
@Data
public class PlatformUserRoleDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 平台用户id
	 */
	private Long userId;
	/**
	 * 角色id
	 */
	private List<Long> roleIds;

}
