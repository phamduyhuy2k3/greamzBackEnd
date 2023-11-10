package com.greamz.backend.controller;

import com.greamz.backend.security.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class MvcRoute {
    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

}
