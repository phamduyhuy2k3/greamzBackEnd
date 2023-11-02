package com.greamz.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcRoute {
    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }
}
