package com.example.app1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app1.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    // 继承IService后即可使用MyBatis-Plus提供的通用服务方法
} 