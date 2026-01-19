package com.skinzen.notifications.service.idempotency;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryIdempotencyService implements IdempotencyService {

    private final Set<String> processedKeys =
            ConcurrentHashMap.newKeySet();

    @Override
    public boolean isDuplicate(String key) {
        // add() returns false if already exists
        return !processedKeys.add(key);
    }
}
