package com.smartledger.smartledger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController
{
    @GetMapping("/health")
    public String health()
    {
        return "SmartLedger AI is running!";
    }
}
