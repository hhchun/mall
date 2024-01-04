package com.hhchun.mall.user.shop.provider.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.base.Preconditions;
import com.hhchun.mall.common.exception.UnknownErrorException;
import com.hhchun.mall.access.support.provider.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public class RedisShopAccessPermissionCache extends DefaultShopAccessPermissionCache implements InitializingBean {

    // 所有权限数据在redis中的key
    private static final String REDIS_ALL_PERMISSION_KEY = "user:shop:permission:all";
    // 所有权限数据在redis中过期时间的最小基础值,单位为毫秒
    private static final Long REDIS_ALL_PERMISSION_MIN_EXPIRE = TimeUnit.HOURS.toMillis(12);
    // 操作所有权限数据时防止缓存击穿使用的锁
    private static final Lock REDIS_ALL_PERMISSION_LOCK = new ReentrantLock();

    // 用户权限数据在redis中的key前缀
    private static final String REDIS_USER_PERMISSION_KEY_PREFIX = "user:shop:permission:";
    // 用户权限数据在redis中过期时间的最小基础值,单位为毫秒
    private static final Long REDIS_USER_PERMISSION_MIN_EXPIRE = TimeUnit.HOURS.toMillis(3);
    // 操作所有权限数据时防止缓存击穿使用的锁
    private static final Lock REDIS_USER_PERMISSION_LOCK = new ReentrantLock();

    // 公开权限数据在redis中的key
    private static final String REDIS_OVERT_PERMISSION_KEY = "user:shop:permission:overt";
    // 公开权限数据在redis中过期时间的最小基础值,单位为毫秒
    private static final Long REDIS_OVERT_PERMISSION_MIN_EXPIRE = TimeUnit.HOURS.toMillis(12);
    // 操作公开权限数据时防止缓存击穿使用的锁
    private static final Lock REDIS_OVERT_PERMISSION_LOCK = new ReentrantLock();

    private static final String REDIS_LIST_EMPTY_VALUE = "{}";
    private static final List<String> REDIS_LIST_EMPTY_VALUES = Lists.newArrayList(REDIS_LIST_EMPTY_VALUE);

    // 序列化工具对象
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @see #afterPropertiesSet() 初始化赋值
     */
    private ListOperations<String, String> listOps;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<Permission> getAllPermission() {
        List<String> values = listOps.range(REDIS_ALL_PERMISSION_KEY, 0, -1);
        if (CollectionUtils.isEmpty(values)) {
            REDIS_ALL_PERMISSION_LOCK.lock();
            try {
                values = listOps.range(REDIS_ALL_PERMISSION_KEY, 0, -1);
                if (CollectionUtils.isEmpty(values)) {
                    // 调用父类方法获取数据
                    List<Permission> permissions = super.getAllPermission();
                    values = permissions.stream().map(p -> {
                        try {
                            return objectMapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                            throw new UnknownErrorException(e);
                        }
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(values)) {
                        values = REDIS_LIST_EMPTY_VALUES;
                    }
                    listOps.leftPushAll(REDIS_ALL_PERMISSION_KEY, values);
                    // 设置过期时间
                    long random = RandomUtils.nextLong(1, TimeUnit.HOURS.toMillis(REDIS_ALL_PERMISSION_MIN_EXPIRE));
                    long timeout = REDIS_ALL_PERMISSION_MIN_EXPIRE + random;
                    redisTemplate.expire(REDIS_ALL_PERMISSION_KEY, timeout, TimeUnit.MILLISECONDS);
                }
            } finally {
                REDIS_ALL_PERMISSION_LOCK.unlock();
            }
        }
        return deserializePermissions(values);
    }

    @Override
    public List<Permission> getOwnedPermission(Long shopUserId) {
        Preconditions.checkArgument(shopUserId != null, "shopUserId == null!");
        String key = REDIS_USER_PERMISSION_KEY_PREFIX + shopUserId;
        List<String> values = listOps.range(key, 0, -1);
        if (CollectionUtils.isEmpty(values)) {
            REDIS_USER_PERMISSION_LOCK.lock();
            try {
                values = listOps.range(key, 0, -1);
                if (CollectionUtils.isEmpty(values)) {
                    List<Permission> permissions = super.getOwnedPermission(shopUserId);
                    values = permissions.stream().map(p -> {
                        try {
                            return objectMapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                            throw new UnknownErrorException(e);
                        }
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(values)) {
                        values = REDIS_LIST_EMPTY_VALUES;
                    }
                    listOps.leftPushAll(key, values);
                    // 设置过期时间
                    long random = RandomUtils.nextLong(1, TimeUnit.HOURS.toMillis(REDIS_USER_PERMISSION_MIN_EXPIRE));
                    long timeout = REDIS_USER_PERMISSION_MIN_EXPIRE + random;
                    redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
                }
            } finally {
                REDIS_USER_PERMISSION_LOCK.unlock();
            }
        }
        return deserializePermissions(values);
    }

    @Override
    public List<Permission> getOvertPermission() {
        List<String> values = listOps.range(REDIS_OVERT_PERMISSION_KEY, 0, -1);
        if (CollectionUtils.isEmpty(values)) {
            REDIS_OVERT_PERMISSION_LOCK.lock();
            try {
                values = listOps.range(REDIS_OVERT_PERMISSION_KEY, 0, -1);
                if (CollectionUtils.isEmpty(values)) {
                    List<Permission> permissions = super.getOvertPermission();
                    values = permissions.stream().map(p -> {
                        try {
                            return objectMapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                            throw new UnknownErrorException(e);
                        }
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(values)) {
                        values = REDIS_LIST_EMPTY_VALUES;
                    }
                    listOps.leftPushAll(REDIS_OVERT_PERMISSION_KEY, values);
                    // 设置过期时间
                    long random = RandomUtils.nextLong(1, TimeUnit.HOURS.toMillis(REDIS_OVERT_PERMISSION_MIN_EXPIRE));
                    long timeout = REDIS_OVERT_PERMISSION_MIN_EXPIRE + random;
                    redisTemplate.expire(REDIS_OVERT_PERMISSION_KEY, timeout, TimeUnit.MILLISECONDS);
                }
            } finally {
                REDIS_OVERT_PERMISSION_LOCK.unlock();
            }
        }
        return deserializePermissions(values);
    }

    @Override
    public void invalidateAllPermission() {
        redisTemplate.delete(REDIS_ALL_PERMISSION_KEY);
    }

    @Override
    public void invalidateOvertPermission() {
        redisTemplate.delete(REDIS_OVERT_PERMISSION_KEY);
    }

    @Override
    public void invalidateOwnedPermission(Long shopUserId) {
        String key = REDIS_USER_PERMISSION_KEY_PREFIX + shopUserId;
        redisTemplate.delete(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (listOps == null) {
            listOps = redisTemplate.opsForList();
        }
    }

    private List<Permission> deserializePermissions(List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return Lists.newArrayList();
        }
        return values.stream()
                .filter(value -> !REDIS_LIST_EMPTY_VALUE.equals(value))
                .map(value -> {
                    try {
                        return objectMapper.readValue(value, Permission.TYPE_REFERENCE);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                        throw new UnknownErrorException(e);
                    }
                }).collect(Collectors.toList());
    }

}
