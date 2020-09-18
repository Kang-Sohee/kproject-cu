package com.ohdocha.cu.kprojectcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class KprojectCuApplication {

    public static void main(String[] args) {
        SpringApplication.run(KprojectCuApplication.class, args);
    }
}
