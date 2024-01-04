package com.hhchun.mall.user.shop.provider.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.exception.UnknownErrorException;
import com.hhchun.mall.access.support.provider.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LocalShopAccessPermissionCache extends RedisShopAccessPermissionCache {
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

    public LocalShopAccessPermissionCache() {
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
    public List<Permission> getOwnedPermission(Long shopUserId) {
        Preconditions.checkArgument(shopUserId != null, "shopUserId == null!");
        try {
            String key = String.valueOf(shopUserId);
            return CACHE.get(key, () -> super.getOwnedPermission(shopUserId));
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


    @Override
    public void invalidateAllPermission() {
        super.invalidateAllPermission();
        CACHE.invalidate(LOCAL_ALL_PERMISSION_KEY);
    }

    @Override
    public void invalidateOvertPermission() {
        super.invalidateOvertPermission();
        CACHE.invalidate(LOCAL_OVERT_PERMISSION_KEY);
    }

    @Override
    public void invalidateOwnedPermission(Long shopUserId) {
        super.invalidateOwnedPermission(shopUserId);
        String key = String.valueOf(shopUserId);
        CACHE.invalidate(key);
    }
}
