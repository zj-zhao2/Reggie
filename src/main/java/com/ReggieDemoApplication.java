package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.dao")
@SpringBootApplication
@EnableTransactionManagement
public class ReggieDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieDemoApplication.class, args);
    }

}
