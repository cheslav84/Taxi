package com.havryliuk.util.google.map;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.havryliuk.persistence.model.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Component
public class DurationResolver {

    @Value( "${google.api.key}" )
    private String apiKey;
    private GeoApiContext context;

    public Optional<Duration> getDuration(Trip trip)  {
        context = getInstance();
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context);
        Instant departureTime = getDepartureTime(trip);
        request.departureTime(departureTime);
        DistanceMatrix distanceMatrix = getDistanceMatrix(trip, request);
        return mapDuration(distanceMatrix);
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

    private Instant getDepartureTime(Trip trip) {
        LocalDateTime departureTime = trip.getDepartureDateTime();
        return departureTime.toInstant(ZoneOffset.UTC);
    }

    private DistanceMatrix getDistanceMatrix(Trip trip, DistanceMatrixApiRequest request) {
        DistanceMatrix distanceMatrix = null;
        try {
            request.mode(TravelMode.DRIVING)
                    .trafficModel(TrafficModel.BEST_GUESS)
                    .units(Unit.METRIC)
                    .language("en-US");
            setAddressOrLocation(trip, request);
            distanceMatrix = request.await();
        } catch (ApiException | InterruptedException | IOException e) {
            log.error("DistanceResolver failed.", e);
        }
        return distanceMatrix;
    }

    private void setAddressOrLocation(Trip trip, DistanceMatrixApiRequest request) {
        if (isLocationPresent(trip)) {
            request.origins(trip.getOriginAddress().getLocation())
                    .destinations(trip.getDestinationAddress().getLocation());
        } else {
            request.origins(trip.getOriginAddress().getAddress())
                    .destinations(trip.getDestinationAddress().getAddress());
        }
    }

    private boolean isLocationPresent(Trip trip) {
        return trip.getOriginAddress().getLocation() != null
                && trip.getDestinationAddress().getLocation() != null;
    }

    private Optional<Duration> mapDuration(DistanceMatrix response) {
        Duration duration = null;
        try {
            DistanceMatrixRow row = response.rows[0];
            DistanceMatrixElement element = row.elements[0];
            duration = element.durationInTraffic;
        } catch (Exception e) {
            log.error("Error during calculation traveling duration.", e);
        }
        return Optional.of(duration);
    }

    @PreDestroy
    public void closeGeoApiContext() {
        context.shutdown();
    }
}
