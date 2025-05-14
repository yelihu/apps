package com.example.common.service;

import com.example.common.config.CommonServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * HelloWorld服务，提供基本的问候功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HelloWorldService {
    
    private final CommonServiceProperties properties;
    
    /**
     * 获取欢迎消息
     * @return 配置的欢迎消息
     */
    public String getWelcomeMessage() {
        log.info("HelloWorldService: 返回欢迎消息");
        return properties.getWelcomeMessage();
    }
    
    /**
     * 个性化问候
     * @param name 用户名
     * @return 包含用户名的问候语
     */
    public String greet(String name) {
        String message = "你好, " + name + "! " + properties.getWelcomeMessage();
        log.info("HelloWorldService: 向用户 '{}' 发送问候", name);
        return message;
    }
} 