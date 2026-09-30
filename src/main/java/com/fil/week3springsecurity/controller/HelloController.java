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
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String accessToken ;
        String refreshToken ;
        if (username.equals("apiuser") && password.equals("apipassword")) {
            accessToken = jwtUtil.generateToken(username, List.of("ROLE_USER" ),15);
            refreshToken = jwtUtil.generateToken(username, List.of("ROLE_USER"), 60 * 24);
            return Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            );
        }
        if (username.equals("admin") && password.equals("adminpassword")) {
            accessToken = jwtUtil.generateToken(username, List.of("ROLE_ADMIN" ),15);
            refreshToken = jwtUtil.generateToken(username, List.of("ROLE_ADMIN"), 60 * 24);
            return Map.of(
                    "access_token", accessToken,
                    "refresh_token", refreshToken
            );
        }
        return null;
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        String username = jwtUtil.extractUserName(refreshToken);
        if (jwtUtil.validateToken(refreshToken, username)) {
            List<String> roles = jwtUtil.parseClaimValue(refreshToken).get("roles", List.class);
            String newAccessToken = jwtUtil.generateToken(username, roles, 15);
            return newAccessToken;
        }
        return null;
    }
}
