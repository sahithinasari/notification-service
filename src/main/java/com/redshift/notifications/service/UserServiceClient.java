package com.redshift.notifications.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-MANAGEMENT-SYSTEM")  // Use only the service name
public interface UserServiceClient {

    @GetMapping("/auth/id/{userId}")
    String getUserEmail(@PathVariable String userId);
}
