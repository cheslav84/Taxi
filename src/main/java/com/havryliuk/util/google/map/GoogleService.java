package com.havryliuk.util.google.map;

import com.google.maps.model.LatLng;
import com.havryliuk.model.Address;
import com.havryliuk.model.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GoogleService {
    private final Geocoder geocoder;
    private final DistanceAndDurationResolver resolver;

    public GoogleService(@Autowired Geocoder geocoder, @Autowired DistanceAndDurationResolver resolver) {
        this.geocoder = geocoder;
        this.resolver = resolver;
    }

    public void setAddressLocation(Address address) {
        LatLng location = geocoder.getLocation(address.getAddress());
        address.setLatitude(location.lat);
        address.setLongitude(location.lng);
    }

    public void setDistanceAndDuration(Trip trip) {
        try {
            DistanceAndDuration distanceAndDuration = resolver.getDistanceAndDuration(trip);
            long distanceInMeters = distanceAndDuration.getDistance().inMeters;
            long durationInSeconds = distanceAndDuration.getDuration().inSeconds;
            trip.setDistanceInMeters(distanceInMeters);
            trip.setDurationInSeconds(durationInSeconds);
        } catch (Exception e) {
            log.warn("Unable to get distance, location, or both.", e);
        }
    }

}
