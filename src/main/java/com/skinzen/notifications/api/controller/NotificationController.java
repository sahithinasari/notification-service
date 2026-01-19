package com.skinzen.notifications.api.controller;

import com.skinzen.notifications.api.dto.NotificationRequest;
import com.skinzen.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/com/skinzen/notifications/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /*
    * POST /api/notifications
{
  "userId": "U123",
  "eventType": "ORDER_PLACED",
  "channels": ["EMAIL"],
  "recipient": "priyamvadhanasari@gmail.com",
  "params": {
    "orderId": "ORD1001",
    "amount": "1999"
  }
}
*/
    /*
    * {
  "notificationId": "uuid-123",
  "userId": "U1001",
  "eventType": "OTP",
  "channels": ["EMAIL"],
  "recipient": "priyamvadhanasari@gmail.com",
  "params": {
    "otp": "483921",
    "expiresIn": "120"
  }
}
*/
    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody NotificationRequest request) {
       notificationService.processNotification(request);
        return ResponseEntity.accepted().body(Map.of("notificationId", request.getUserId()));
    }
}
