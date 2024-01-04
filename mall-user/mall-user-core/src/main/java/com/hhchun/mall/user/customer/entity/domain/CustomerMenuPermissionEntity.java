package com.hhchun.mall.user.customer.entity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 顾客菜单关联权限
 * 
 * @author hhchun
 * @email 12487489@qq.com
 * @date 2023-12-09 03:22:30
 */
@Data
@TableName("user_customer_menu_permission")
public class CustomerMenuPermissionEntity implements Serializable {
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
	 * 菜单id
	 */
	private Long menuId;
	/**
	 * 权限id
	 */
	private Long permissionId;

}
