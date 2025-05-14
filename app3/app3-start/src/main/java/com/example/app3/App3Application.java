package com.example.app3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.app3", "com.example.common"})
public class App3Application {

    public static void main(String[] args) {
        SpringApplication.run(App3Application.class, args);
    }

}
