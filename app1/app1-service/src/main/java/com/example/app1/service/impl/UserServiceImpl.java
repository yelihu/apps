package com.example.app1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.app1.entity.User;
import com.example.app1.mapper.UserMapper;
import com.example.app1.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // 继承ServiceImpl后即可使用MyBatis-Plus提供的通用服务实现
} 