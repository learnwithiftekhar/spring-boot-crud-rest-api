package com.learnwithiftekhar.spring_boot_rest_api.controller;

import com.learnwithiftekhar.spring_boot_rest_api.service.RateLimitingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ratelimit")
public class AdminController {

    private final RateLimitingService rateLimitingService;

    public AdminController(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @PostMapping("/toggle")
    public String toggleRateLimit(@RequestParam boolean enabled) {
        rateLimitingService.setRateLimitEnabled(enabled);
        return "Rate limiting is now: " + (enabled ? "ENABLED" : "DISABLED");
    }

    @GetMapping("/status")
    public boolean getStatus() {
        return rateLimitingService.isRateLimitEnabled();
    }
}