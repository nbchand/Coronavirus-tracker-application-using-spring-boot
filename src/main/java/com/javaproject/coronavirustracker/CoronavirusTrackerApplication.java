package com.javaproject.coronavirustracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//this tells spring to run scheduled method in the services
public class CoronavirusTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoronavirusTrackerApplication.class, args);
    }

}
