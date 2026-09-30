package com.fil.week3springsecurity.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class HelloService {


    //ADMIN
    @Secured("ROLE_USER")
    public String sayHelloByAdmin() {
        System.out.println("Hello Admin");
        return "Hello World!";
    }

    @Secured("ROLE_USER")
    public String sayHelloByUser() {
        System.out.println("Hello User");
        return "Hello World!";
    }
}
