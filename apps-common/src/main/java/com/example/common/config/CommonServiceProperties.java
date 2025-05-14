package com.example.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 公共服务模块的配置属性
 */
@Data
@ConfigurationProperties(prefix = "common.service")
public class CommonServiceProperties {
    
    /**
     * 是否启用HelloWorld服务
     */
    private boolean helloWorldEnabled = true;
    
    /**
     * HelloWorld服务的欢迎消息
     */
    private String welcomeMessage = "Hello from Common Service Module!";
} 