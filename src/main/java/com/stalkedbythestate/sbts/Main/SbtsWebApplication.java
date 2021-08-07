package com.stalkedbythestate.sbts.Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.stalkedbythestate"})
public class SbtsWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbtsWebApplication.class, args);
    }
}
