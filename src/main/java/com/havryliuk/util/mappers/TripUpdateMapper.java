package com.havryliuk.util.mappers;

import com.havryliuk.dto.trips.TripDtoForPassengerUpdate;
import com.havryliuk.model.*;
import com.havryliuk.util.google.map.GoogleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class TripUpdateMapper {

    private final GoogleService googleService;

    @Autowired
    public TripUpdateMapper(GoogleService googleService) {
        this.googleService = googleService;
    }

    public void setUpdatedValues(Trip trip, TripDtoForPassengerUpdate tripDto) {
        trip.setCarClass(tripDto.getCarClass());
        boolean updateDepartureTimeWasUpdated = updateDepartureTime(trip, tripDto);
        boolean originAddressWasUpdated = updateOriginAddress(trip, tripDto);
        boolean destinationAddressWasUpdated = updateDestinationAddress(trip, tripDto);
        if (updateDepartureTimeWasUpdated || originAddressWasUpdated || destinationAddressWasUpdated) {
            googleService.setDistanceAndDuration(trip);
            log.info("Trip {} updates distance {}, and duration {}.",
                    tripDto, trip.getDistanceInMeters(), trip.getDurationInSeconds());
        }
    }

    private boolean updateDepartureTime(Trip trip, TripDtoForPassengerUpdate tripDto) {
        boolean wasUpdated = false;
        LocalDateTime initialDepartureDateTime = trip.getDepartureDateTime();
        LocalDateTime newDepartureDateTime = tripDto.getDepartureDateTime();
        if (!initialDepartureDateTime.equals(newDepartureDateTime)) {
            trip.setDepartureDateTime(tripDto.getDepartureDateTime());
            wasUpdated = true;
            log.info("Trip {} updates departure time.", tripDto);
        }
        return wasUpdated;
    }

    private boolean updateOriginAddress(Trip trip, TripDtoForPassengerUpdate tripDto) {
        boolean wasUpdated = false;
        Address originAddress = trip.getOriginAddress();
        String receivedOriginAddress = tripDto.getOriginAddress();
        if (isAddressWasUpdated(originAddress, receivedOriginAddress)) {
            Address newAddress = getNewAddress(receivedOriginAddress);
            trip.setOriginAddress(newAddress);
            wasUpdated = true;
            log.info("Trip {} updates origin address.", tripDto);
        }
        return wasUpdated;
    }

    private boolean updateDestinationAddress(Trip trip, TripDtoForPassengerUpdate tripDto) {
        boolean wasUpdated = false;
        Address destinationAddress = trip.getDestinationAddress();
        String receivedDestinationAddress = tripDto.getDestinationAddress();
        if (isAddressWasUpdated(destinationAddress, receivedDestinationAddress)) {
            Address newAddress = getNewAddress(receivedDestinationAddress);
            trip.setDestinationAddress(newAddress);
            wasUpdated = true;
            log.info("Trip {} updates destination address.", tripDto);
        }
        return wasUpdated;
    }

    private Address getNewAddress(String receivedAddress) {
        Address address = new Address();
        address.setAddress(receivedAddress);
        googleService.setAddressLocation(address);
        return address;
    }

    private boolean isAddressWasUpdated(Address originAddress, String receivedOriginAddress) {
        return !originAddress.getAddress().equals(receivedOriginAddress);
    }


}
