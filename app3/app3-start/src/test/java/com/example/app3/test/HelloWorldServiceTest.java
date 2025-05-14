package com.example.app3.test;

import com.example.common.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * HelloWorld服务测试类 - App3
 */
@Slf4j
@SpringBootTest
@ComponentScan(basePackages = {"com.example.app3", "com.example.common"})
public class HelloWorldServiceTest {


    @Autowired
    private HelloWorldService helloWorldService;

    /**
     * 测试获取欢迎消息
     */
    @Test
    public void testGetWelcomeMessage() {
        String welcomeMessage = helloWorldService.getWelcomeMessage();
        log.info("欢迎消息: {}", welcomeMessage);
        
        // 输出结果到控制台
        System.out.println("===============================");
        System.out.println("App3 - 欢迎消息: " + welcomeMessage);
        System.out.println("===============================");
    }

    /**
     * 测试个性化问候功能
     */
    @Test
    public void testGreet() {
        String name = "王五";
        String greetMessage = helloWorldService.greet(name);
        log.info("对 {} 的问候: {}", name, greetMessage);
        
        // 输出结果到控制台
        System.out.println("===============================");
        System.out.println("App3 - 个性化问候: " + greetMessage);
        System.out.println("===============================");
    }
} 