package com.example.app1.service;

import com.example.common.service.HelloWorldService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yelihu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HelloWorldProxyService {

    @Autowired
    private HelloWorldService helloWorldService;


    @PostConstruct
    public void init() {
        String msg = helloWorldService.getWelcomeMessage();
        log.info("HelloWorldProxyService: 初始化:{}", msg);
    }

    public HelloWorldProxyService(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }
}
