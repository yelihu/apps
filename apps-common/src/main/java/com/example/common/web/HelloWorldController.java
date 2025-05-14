package com.example.common.web;

import com.example.common.service.HelloWorldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloWorld服务的REST控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/common/hello")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "common.service", name = "hello-world-enabled", havingValue = "true", matchIfMissing = true)
public class HelloWorldController {

    private final HelloWorldService helloWorldService;
    
    /**
     * 获取欢迎消息
     * @return 欢迎消息
     */
    @GetMapping
    public String getWelcomeMessage() {
        log.info("REST请求: 获取欢迎消息");
        return helloWorldService.getWelcomeMessage();
    }
    
    /**
     * 获取个性化问候
     * @param name 用户名
     * @return 包含用户名的问候语
     */
    @GetMapping("/{name}")
    public String greet(@PathVariable String name) {
        log.info("REST请求: 向 '{}' 发送问候", name);
        return helloWorldService.greet(name);
    }
}