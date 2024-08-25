package com.jy.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jy
 */
@MapperScan("com.jy.ai.mapper")
@SpringBootApplication
public class SpringAIApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAIApplication.class, args);
    }
}
