package com.dzhaparov.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class SevemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SevemApplication.class, args);
    }

}
