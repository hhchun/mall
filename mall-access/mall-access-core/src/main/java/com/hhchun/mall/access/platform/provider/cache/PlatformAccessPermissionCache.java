package com.hhchun.mall.access.platform.provider.cache;

import com.hhchun.mall.access.support.provider.Permission;
import java.util.List;

/**
 * 外观者模式
 * 用于封装权限相关数据在缓存中的操作逻辑
 */
public interface PlatformAccessPermissionCache {

    List<Permission> getAllPermission();

    List<Permission> getOwnedPermission(Long platformUserId);

    List<Permission> getOvertPermission();
}
