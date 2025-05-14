package com.example.common.config;

import com.example.common.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 公共服务模块的自动配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CommonServiceProperties.class)
@ComponentScan("com.example.common")
public class CommonServiceAutoConfiguration {
    
    public CommonServiceAutoConfiguration() {
        log.info("初始化公共服务模块自动配置");
    }
    
    /**
     * 条件性地配置HelloWorldService
     * 只有在common.service.hello-world-enabled=true时才会创建
     */
    @Bean
    @ConditionalOnProperty(prefix = "common.service", name = "hello-world-enabled", havingValue = "true", matchIfMissing = true)
    public HelloWorldService helloWorldService(CommonServiceProperties properties) {
        log.info("注册HelloWorldService");
        return new HelloWorldService(properties);
    }
} 