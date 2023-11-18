package com.greamz.backend.controller;

import com.greamz.backend.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessLogController {
    @Autowired
    private AccessLogService accessLogService;

    @GetMapping("/access")
    public String yourEndpoint(@RequestParam String ipAddress) {
        // Do something with the request

        // Save access log
        accessLogService.saveAccessLog(ipAddress);

        return "Your response";
    }
}
