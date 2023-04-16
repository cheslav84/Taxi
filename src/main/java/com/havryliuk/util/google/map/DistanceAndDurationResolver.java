package com.havryliuk.util.google.map;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.havryliuk.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component
public class DistanceAndDurationResolver {

    private final GeoApiContext context;

    @Autowired
    public DistanceAndDurationResolver(GeoApiContext context) {
        this.context = context;
    }

    public DistanceAndDuration getDistanceAndDuration(
            LocalDateTime departureTime, Address originAddress, Address destinationAddress)  {
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context);
        request.departureTime(departureTime.toInstant(ZoneOffset.UTC));
        DistanceMatrix distanceMatrix = getDistanceMatrix(originAddress, destinationAddress, request);
        return mapDistanceAndDuration(distanceMatrix);
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
