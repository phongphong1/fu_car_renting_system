package com.fpt.rentingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentingServiceApplication.class, args);
    }

}
