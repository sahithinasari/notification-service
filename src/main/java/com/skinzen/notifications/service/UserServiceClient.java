package com.skinzen.notifications.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:2023")
public interface UserServiceClient {

    @GetMapping("/auth/id/{userId}")
    String getUserEmail(@PathVariable String userId);
}
