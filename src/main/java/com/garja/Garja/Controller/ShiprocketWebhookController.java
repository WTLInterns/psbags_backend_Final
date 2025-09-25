package com.garja.Garja.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.garja.Garja.DTO.requests.ShiprocketWebhookPayload;

@RestController
@RequestMapping("/webhook")
public class ShiprocketWebhookController {

    private static final String SECRET_TOKEN = "my-secret-token-9876"; 
    private static final String TRACKED_ORDER_ID = "363679128452"; 

    @PostMapping("/shiprocket")
    public ResponseEntity<String> handleWebhook(
            @RequestBody ShiprocketWebhookPayload payload,
            @RequestHeader(value = "x-api-key", required = false) String token) {

        if (token == null || !SECRET_TOKEN.equals(token)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        String orderId = payload.getOrder_id();
        String status = payload.getCurrent_status();

        if (TRACKED_ORDER_ID.equals(orderId)) {
            System.out.println("📦 Update for tracked order: " + orderId);
            System.out.println("➡ Status: " + status);
            System.out.println("➡ Courier: " + payload.getCourier_name());
            System.out.println("➡ Timeline: " + payload.getScans());

        } else {
            System.out.println("Ignoring other order: " + orderId);
        }

        return new ResponseEntity<>("Webhook received", HttpStatus.OK);
    }
}

