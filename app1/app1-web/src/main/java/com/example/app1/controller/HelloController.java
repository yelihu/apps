package com.example.app1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允许所有来源的跨域请求，生产环境应该限制来源
public class HelloController {

    @GetMapping("/hello")
    public String getHelloWorld() {
        return "Hello World from App1!";
    }
} 