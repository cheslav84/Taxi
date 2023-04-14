package com.havryliuk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        log.trace("PasswordEncoder requested.");
        return new BCryptPasswordEncoder();
    }
}
