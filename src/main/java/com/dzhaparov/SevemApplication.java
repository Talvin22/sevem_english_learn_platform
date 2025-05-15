package com.dzhaparov;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class SevemApplication {

    public static void main(String[] args) {


        if (System.getenv("RENDER") == null) {
            Dotenv dotenv = Dotenv.load();
            System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
            System.setProperty("DB_USER", dotenv.get("DB_USER"));
            System.setProperty("DB_PASS", dotenv.get("DB_PASS"));
            System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
            System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        }

        SpringApplication.run(SevemApplication.class, args);
    }
}