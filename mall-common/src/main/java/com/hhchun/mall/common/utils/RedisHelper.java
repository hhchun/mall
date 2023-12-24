package com.hhchun.mall.common.utils;

import com.sun.istack.internal.NotNull;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.stream.Collectors;

public class RedisHelper {
    /**
     * scan实现
     *
     * @param redisTemplate redisTemplate
     * @param pattern       表达式
     */
    public static List<String> scan(RedisTemplate<String, ?> redisTemplate, @NotNull String pattern) {
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(2)
                .build();
        Cursor<String> cursor = redisTemplate.scan(options);
        return cursor.stream().collect(Collectors.toList());
    }
}
