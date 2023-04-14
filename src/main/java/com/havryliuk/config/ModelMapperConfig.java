package com.havryliuk.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        log.trace("ModelMapper requested.");
        return new ModelMapper();
    }
}
