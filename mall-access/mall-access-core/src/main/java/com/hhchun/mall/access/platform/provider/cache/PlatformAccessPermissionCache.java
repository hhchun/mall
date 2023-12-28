package com.hhchun.mall.access.platform.provider.cache;

import com.hhchun.mall.access.support.provider.Permission;

import java.util.List;

/**
 * 外观者模式
 * 用于封装权限相关数据在缓存中的操作逻辑
 */
public interface PlatformAccessPermissionCache {

    /**
     * 从缓存中获取所有权限数据
     *
     * @return 所有权限数据
     */
    List<Permission> getAllPermission();

    /**
     * 根据用户id从缓存中获取权限数据
     *
     * @param platformUserId 用户id
     * @return 用户的权限数据
     */
    List<Permission> getOwnedPermission(Long platformUserId);

    /**
     * 从缓存中获取公开权限数据
     *
     * @return 公开的权限数据
     */
    List<Permission> getOvertPermission();

    /**
     * 使所有权限数据在缓存中失效
     */
    void invalidateAllPermission();

    /**
     * 使用户权限数据在缓存中失效
     *
     * @param platformUserId 用户id
     */
    void invalidateOwnedPermission(Long platformUserId);

    /**
     * 使公开权限数据在缓存中失效
     */
    void invalidateOvertPermission();

}
