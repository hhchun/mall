package com.hhchun.mall.access.platform.provider.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhchun.mall.access.common.base.Preconditions;
import com.hhchun.mall.access.exception.UnknownErrorException;
import com.hhchun.mall.access.support.provider.Permission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public class RedisPlatformAccessPermissionCache extends DefaultPlatformAccessPermissionCache implements InitializingBean {

    // 所有权限数据在redis中的key
    private static final String REDIS_ALL_PERMISSION_KEY = "access:platform:permission:all";
    // 所有权限数据在redis中过期时间的最小基础值,单位为毫秒
    private static final Long REDIS_ALL_PERMISSION_MIN_EXPIRE = TimeUnit.HOURS.toMillis(12);
    // 操作所有权限数据时防止缓存击穿使用的锁
    private static final Lock REDIS_ALL_PERMISSION_LOCK = new ReentrantLock();

    // 用户权限数据在redis中的key前缀
    private static final String REDIS_USER_PERMISSION_KEY_PREFIX = "access:platform:permission:";
    // 用户权限数据在redis中过期时间的最小基础值,单位为毫秒
    private static final Long REDIS_USER_PERMISSION_MIN_EXPIRE = TimeUnit.HOURS.toMillis(3);
    // 操作所有权限数据时防止缓存击穿使用的锁
    private static final Lock REDIS_USER_PERMISSION_LOCK = new ReentrantLock();

    // 公开权限数据在redis中的key
    private static final String REDIS_OVERT_PERMISSION_KEY = "access:platform:permission:overt";
    // 公开权限数据在redis中过期时间的最小基础值,单位为毫秒
    private static final Long REDIS_OVERT_PERMISSION_MIN_EXPIRE = TimeUnit.HOURS.toMillis(12);
    // 操作公开权限数据时防止缓存击穿使用的锁
    private static final Lock REDIS_OVERT_PERMISSION_LOCK = new ReentrantLock();

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
        if (values == null) {
            REDIS_ALL_PERMISSION_LOCK.lock();
            try {
                values = listOps.range(REDIS_ALL_PERMISSION_KEY, 0, -1);
                if (values == null) {
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
        return values.stream().map(value -> {
            try {
                return objectMapper.readValue(value, Permission.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new UnknownErrorException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOwnedPermission(Long platformUserId) {
        Preconditions.checkArgument(platformUserId != null, "platformUserId == null!");
        String key = REDIS_USER_PERMISSION_KEY_PREFIX + platformUserId;
        List<String> values = listOps.range(key, 0, -1);
        if (values == null) {
            REDIS_USER_PERMISSION_LOCK.lock();
            try {
                values = listOps.range(key, 0, -1);
                if (values == null) {
                    List<Permission> permissions = super.getOwnedPermission(platformUserId);
                    values = permissions.stream().map(p -> {
                        try {
                            return objectMapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                            throw new UnknownErrorException(e);
                        }
                    }).collect(Collectors.toList());
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
        return values.stream().map(value -> {
            try {
                return objectMapper.readValue(value, Permission.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new UnknownErrorException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<Permission> getOvertPermission() {
        List<String> values = listOps.range(REDIS_OVERT_PERMISSION_KEY, 0, -1);
        if (values == null) {
            REDIS_OVERT_PERMISSION_LOCK.lock();
            try {
                values = listOps.range(REDIS_OVERT_PERMISSION_KEY, 0, -1);
                if (values == null) {
                    List<Permission> permissions = super.getOvertPermission();
                    values = permissions.stream().map(p -> {
                        try {
                            return objectMapper.writeValueAsString(p);
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                            throw new UnknownErrorException(e);
                        }
                    }).collect(Collectors.toList());
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
        return values.stream().map(value -> {
            try {
                return objectMapper.readValue(value, Permission.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new UnknownErrorException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (listOps == null) {
            listOps = redisTemplate.opsForList();
        }
    }
}
