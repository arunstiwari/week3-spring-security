package com.fil.week3springsecurity.controller;

import com.fil.week3springsecurity.service.HelloService;
import com.fil.week3springsecurity.util.JWTUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    private HelloService helloService;
    JWTUtil jwtUtil;

    public HelloController(JWTUtil jwtUtil, HelloService helloService) {
        this.jwtUtil = jwtUtil;
        this.helloService = helloService;
    }

    @GetMapping("/public")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Hello World! Secured";
    }

    @GetMapping("/user")
    public String user() {
        helloService.sayHelloByUser();
        return "Hello World! You have User Role";
    }

    @GetMapping("/admin")
    public String admin() {
        helloService.sayHelloByAdmin();
        return "Hello World! You have Admin Role";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username.equals("apiuser") && password.equals("apipassword")) {
            return jwtUtil.generateToken(username, List.of("ROLE_USER"));
        }
        if (username.equals("admin") && password.equals("adminpassword")) {
            return jwtUtil.generateToken(username, List.of("ROLE_ADMIN"));
        }
        return "Login Failed";
    }
}
