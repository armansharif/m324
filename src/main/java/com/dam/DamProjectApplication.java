package com.dam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class DamProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DamProjectApplication.class, args);
    }

}
