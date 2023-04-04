package com.havryliuk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.havryliuk.repository")
@EntityScan("com.havryliuk.model")
@SpringBootApplication
public class TaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiApplication.class, args);
    }

}
