package com.havryliuk.config;

import com.google.maps.GeoApiContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GeoApiContextConfig {

    @Value( "${google.api.key}" )
    private String apiKey;

    @Bean
    public GeoApiContext geoApiContext() {
        log.trace("ModelMapper requested.");
        return new GeoApiContext.Builder().apiKey(apiKey).build();
    }

}
