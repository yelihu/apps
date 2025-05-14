package com.example.app1.controller;

import com.example.app1.entity.User;
import com.example.app1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取所有用户
     */
    @GetMapping
    public List<User> listAll() {
        return userService.list();
    }
} 