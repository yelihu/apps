package com.example.app1.test;

import com.example.app1.App1Application;
import com.example.common.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * HelloWorld服务运行器
 * 直接运行此类来测试HelloWorld服务
 */
@Slf4j
public class HelloWorldServiceRunner {

    public static void main(String[] args) {
        log.info("启动应用并测试HelloWorld服务...");
        
        // 启动Spring应用
        ConfigurableApplicationContext context = SpringApplication.run(App1Application.class, args);
        
        try {
            // 获取HelloWorldService
            HelloWorldService helloWorldService = context.getBean(HelloWorldService.class);
            
            // 获取欢迎消息
            String welcomeMessage = helloWorldService.getWelcomeMessage();
            log.info("欢迎消息: {}", welcomeMessage);
            System.out.println("===============================");
            System.out.println("欢迎消息: " + welcomeMessage);
            System.out.println("===============================");
            
            // 获取个性化问候
            String name = "张三";
            String greetMessage = helloWorldService.greet(name);
            log.info("个性化问候: {}", greetMessage);
            System.out.println("===============================");
            System.out.println("个性化问候: " + greetMessage);
            System.out.println("===============================");
            
        } finally {
            // 关闭应用上下文
            context.close();
        }
    }
} 