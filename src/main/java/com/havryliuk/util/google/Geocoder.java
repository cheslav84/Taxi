package com.havryliuk.util.google;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Component
public class Geocoder {

    @Value( "${google.api.key}" )
    private String apiKey;

    private GeoApiContext context;


    public LatLng getLocation(String address) {
        LatLng location = null;
        try {
            context = getInstance();
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            location = results[0].geometry.location;
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("Location can't be defined.", e);
        }
        return location;
    }

    private GeoApiContext getInstance() {
        GeoApiContext result = context;
        if (result != null) {
            return result;
        }
        synchronized(GeoApiContext.class) {
            if (context == null) {
                context = new GeoApiContext.Builder()
                        .apiKey(apiKey)
                        .build();
            }
            return context;
        }
    }

    @PreDestroy
    public void closeGeoApiContext() {
        context.shutdown();
    }

}
