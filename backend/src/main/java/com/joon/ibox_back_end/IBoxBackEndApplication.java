package com.joon.ibox_back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IBoxBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(IBoxBackEndApplication.class, args);
    }

}
