package com.fil.week3springsecurity.controller;

import com.fil.week3springsecurity.util.JWTUtil;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class HelloController {

    JWTUtil jwtUtil;
    public HelloController(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/public")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Hello World! Secured";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username.equals("apiuser") && password.equals("apipassword")) {
            return jwtUtil.generateToken(username, null);
        }
        return "Login Failed";
    }
}
