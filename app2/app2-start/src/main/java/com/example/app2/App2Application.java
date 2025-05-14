package com.example.app2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.app2", "com.example.common"})
public class App2Application {

    public static void main(String[] args) {
        SpringApplication.run(App2Application.class, args);
    }

}
