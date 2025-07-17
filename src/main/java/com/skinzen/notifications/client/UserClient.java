package com.skinzen.notifications.client;

import com.skinzen.notifications.dto.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public User getUserById(Long userId) {
        String url = "https://jsonplaceholder.typicode.com/users/" + userId;
        return restTemplate.getForObject(url, User.class);
    }
}
