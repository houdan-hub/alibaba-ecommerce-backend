package com.alibaba.internship;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * E-commerce Backend Application
 *
 * <p>Built during Alibaba internship (Jul–Sep 2024).
 * Covers Oracle→MySQL migration, Spring Boot REST API,
 * Redis caching, and RabbitMQ delayed-order cancellation.</p>
 */
@SpringBootApplication
@MapperScan("com.alibaba.internship.mapper")
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
