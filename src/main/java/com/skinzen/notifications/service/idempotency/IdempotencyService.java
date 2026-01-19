package com.skinzen.notifications.service.idempotency;

public interface IdempotencyService {
    boolean isDuplicate(String key);
}
