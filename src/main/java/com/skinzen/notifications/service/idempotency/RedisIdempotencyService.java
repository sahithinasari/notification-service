package com.skinzen.notifications.service.idempotency;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisIdempotencyService implements IdempotencyService {

    private final StringRedisTemplate redisTemplate;

    private static final Duration TTL = Duration.ofHours(24);
    /*
      Key format : notification:{notificationId}:{channel}
      Example:
        notification:abc123:EMAIL
        notification:abc123:SMS
     */
    @Override
    public boolean isDuplicate(String key) {

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "PROCESSED", TTL);

        // setIfAbsent returns:
        // true  -> key was absent (first time processing)
        // false -> key already exists (duplicate)
        return Boolean.FALSE.equals(success);
    }
}
