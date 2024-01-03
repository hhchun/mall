package com.hhchun.mall.user.platform.service.cache;


import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hhchun.mall.common.exception.UnknownErrorException;
import com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Aspect
public class PlatformMenuServiceCacheAspect {

    private static final String REDIS_USER_MENU_KEY_PREFIX = "user:platform:user:menu:";
    private static final Long REDIS_USER_MENU_MIN_EXPIRE = TimeUnit.HOURS.toMillis(12);
    private static final List<String> REDIS_USER_MENU_EMPTY = Lists.newArrayList("{}");

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(public  java.util.List<com.hhchun.mall.user.platform.entity.vo.PlatformMenuVo> " +
            "com.hhchun.mall.user.platform.service.PlatformMenuService.getPlatformMenusByUserId(Long,boolean,boolean))")
    public Object getPlatformMenusByUserIdFromCache(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Long userId = (Long) args[0];
        boolean upper = (boolean) args[1];
        boolean combine = (boolean) args[2];
        String key = REDIS_USER_MENU_KEY_PREFIX + userId;
        ListOperations<String, String> ops = redisTemplate.opsForList();
        List<String> values = ops.range(key, 0, -1);
        if (CollectionUtils.isEmpty(values)) {
            synchronized (key.intern()) {
                values = ops.range(key, 0, -1);
                if (CollectionUtils.isEmpty(values)) {
                    @SuppressWarnings("unchecked")
                    List<PlatformMenuVo> mvs = (List<PlatformMenuVo>) point.proceed(new Object[]{userId, upper, false});
                    values = mvs.stream().map(mv -> {
                        try {
                            return objectMapper.writeValueAsString(mv);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            throw new UnknownErrorException(e);
                        }
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(values)) {
                        values = REDIS_USER_MENU_EMPTY;
                    }
                    ops.leftPushAll(key, values);
                    long timeout = REDIS_USER_MENU_MIN_EXPIRE + RandomUtil.randomLong(1, REDIS_USER_MENU_MIN_EXPIRE / 2);
                    timeout = values.equals(REDIS_USER_MENU_EMPTY) ? timeout / 60 : timeout;
                    redisTemplate.expire(REDIS_USER_MENU_KEY_PREFIX, timeout, TimeUnit.MILLISECONDS);
                    if (combine && upper) {
                        return PlatformMenuVo.combine(mvs);
                    } else {
                        return mvs;
                    }
                }
            }
        }
        List<PlatformMenuVo> mvs = values.stream().map(value -> {
            try {
                return objectMapper.readValue(value, PlatformMenuVo.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new UnknownErrorException(e);
            }
        }).collect(Collectors.toList());
        if (combine && upper) {
            return PlatformMenuVo.combine(mvs);
        } else {
            return mvs;
        }
    }
}
