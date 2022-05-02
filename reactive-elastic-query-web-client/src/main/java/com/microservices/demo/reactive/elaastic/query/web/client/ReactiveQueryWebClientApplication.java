package com.microservices.demo.reactive.elaastic.query.web.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class ReactiveQueryWebClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveQueryWebClientApplication.class);
    }
}
