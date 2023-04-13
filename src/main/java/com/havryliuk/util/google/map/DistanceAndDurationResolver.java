package com.havryliuk.util.google.map;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.havryliuk.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component
public class DistanceAndDurationResolver {

    @Value( "${google.api.key}" )
    private String apiKey;
    private GeoApiContext context;

    public DistanceAndDuration getDistanceAndDuration(
            LocalDateTime departureTime, Address originAddress, Address destinationAddress)  {
        context = getInstance();
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context);
        request.departureTime(departureTime.toInstant(ZoneOffset.UTC));
        DistanceMatrix distanceMatrix = getDistanceMatrix(originAddress, destinationAddress, request);
        return mapDistanceAndDuration(distanceMatrix);
    }

    private GeoApiContext getInstance() {
        GeoApiContext result = context;
        if (result != null) {
            return result;
        }
        synchronized (GeoApiContext.class) {
            if (context == null) {
                context = new GeoApiContext.Builder().apiKey(apiKey).build();
            }
            return context;
        }
    }

    private DistanceMatrix getDistanceMatrix(
            Address originAddress, Address destinationAddress, DistanceMatrixApiRequest request) {
        DistanceMatrix distanceMatrix = null;
        try {
            request.mode(TravelMode.DRIVING)
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .units(Unit.METRIC)
                    .language("en-US")
                    .origins(originAddress.getAddress())
                    .destinations(destinationAddress.getAddress());
            distanceMatrix = request.await();
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("DistanceResolver failed.", e);
        }
        return distanceMatrix;
    }

    private DistanceAndDuration mapDistanceAndDuration(DistanceMatrix response) {
        DistanceAndDuration distanceAndDuration = new DistanceAndDuration();
        DistanceMatrixRow row = response.rows[0];
        DistanceMatrixElement element = row.elements[0];
        distanceAndDuration.setDuration(element.durationInTraffic);
        distanceAndDuration.setDistance(element.distance);
        return distanceAndDuration;
    }

    @PreDestroy
    public void closeGeoApiContext() {
        context.shutdown();
    }
}
