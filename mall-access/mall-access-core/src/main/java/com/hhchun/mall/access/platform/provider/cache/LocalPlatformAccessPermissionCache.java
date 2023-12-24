package com.hhchun.mall.access.platform.provider.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.exception.UnknownErrorException;
import com.hhchun.mall.access.support.provider.Permission;
import com.hhchun.mall.common.utils.cursor.CollectionQueryCursor;
import com.hhchun.mall.common.utils.cursor.QueryCursor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class LocalPlatformAccessPermissionCache extends RedisPlatformAccessPermissionCache {
    // 所有权限数据在本地缓存中的key
    private static final String LOCAL_ALL_PERMISSION_KEY = "ALL";
    // 公开权限数据在本地缓存中的key
    private static final String LOCAL_OVERT_PERMISSION_KEY = "OVERT";

    // 权限数据写入到本地缓存后的过期时间,单位为毫秒
    private static final Long LOCAL_PERMISSION_EXPIRE_AFTER_WRITE = TimeUnit.HOURS.toMillis(12);
    // 访问本地缓存中的权限数据后的过期时间,单位为毫秒
    private static final Long LOCAL_PERMISSION_EXPIRE_AFTER_ACCESS = TimeUnit.HOURS.toMillis(1);

    // 缓存
    private final Cache<String, List<Permission>> CACHE;

    public LocalPlatformAccessPermissionCache() {
        CACHE = CacheBuilder.newBuilder()
                .expireAfterWrite(LOCAL_PERMISSION_EXPIRE_AFTER_WRITE, TimeUnit.MILLISECONDS)
                .expireAfterAccess(LOCAL_PERMISSION_EXPIRE_AFTER_ACCESS, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public List<Permission> getAllPermission() {
        try {
            return CACHE.get(LOCAL_ALL_PERMISSION_KEY, super::getAllPermission);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new UnknownErrorException(e);
        }
    }

    @Override
    public List<Permission> getOwnedPermission(Long platformUserId) {
        Preconditions.checkArgument(platformUserId != null, "platformUserId == null!");
        try {
            String key = String.valueOf(platformUserId);
            return CACHE.get(key, () -> super.getOwnedPermission(platformUserId));
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new UnknownErrorException(e);
        }
    }

    @Override
    public List<Permission> getOvertPermission() {
        try {
            return CACHE.get(LOCAL_OVERT_PERMISSION_KEY, super::getOvertPermission);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
            throw new UnknownErrorException(e);
        }
    }
}
