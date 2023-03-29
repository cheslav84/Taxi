package com.havryliuk.util.google.map;

import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.havryliuk.persistence.model.Address;
import com.havryliuk.persistence.model.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class GoogleAPI {
    private final Geocoder geocoder;
    private final DurationResolver resolver;

    public GoogleAPI(@Autowired Geocoder geocoder, @Autowired DurationResolver resolver) {
        this.geocoder = geocoder;
        this.resolver = resolver;
    }

    public void setAddressLocation(Address address) {
        LatLng location = geocoder.getLocation(address.getAddress());
        address.setLocation(location);
    }

    public void setDuration(Trip trip) {
        Optional<Duration> duration = resolver.getDuration(trip);
        duration.ifPresent(trip::setApproximateTravelDuration);
    }

}
