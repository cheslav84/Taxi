package com.havryliuk.service.tripService;

import com.havryliuk.dto.trips.*;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.repository.TripRepository;
import com.havryliuk.service.AddressService;
import com.havryliuk.service.PaymentService;
import com.havryliuk.util.google.map.GoogleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Slf4j
@Service
public class DriverTripService {

    private final AddressService addressService;
    private final GoogleService googleService;
    private final TripRepository repository;
    private final PaymentService paymentService;


    @Autowired
    public DriverTripService(AddressService addressService, GoogleService googleService,
                             TripRepository repository, PaymentService paymentService) {
        this.addressService = addressService;
        this.googleService = googleService;
        this.repository = repository;
        this.paymentService = paymentService;
    }

    public Trip getById(String id) {
        log.trace("getById, id={}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.info("trip with id={} hasn't been found.", id);
            return new IllegalArgumentException("Such trip hasn't been found");
        });
    }


    public TripDtoForDriverDetailed getDtoById(String id) {
        log.trace("getDtoById, id={}", id);
        TripDtoForDriverDetailed trip = repository.findDetailedDtoById(id)
                .orElseThrow(() -> {
                    log.info("trip with id={} hasn't been found.", id);
                    return new IllegalArgumentException("Such trip hasn't been found");
                });
        int passengerAge = getAgeFromBirthData(trip.getPassengerBirthDate());
        trip.setPassengerAge(passengerAge);
        return trip;
    }

    public void setStatusDrivingById(String id) {
        log.trace("setStatusDrivingById, id={}", id);
        Trip trip = getById(id);
        trip.setTripStatus(TripStatus.DRIVING);
        repository.save(trip);
        log.debug("Trip status {} has been set to trip {}", TripStatus.DRIVING, id);
    }

    public void setStatusCompletedById(String id) throws PaymentException {
        log.trace("setStatusCompletedById, id={}", id);
        Trip trip = getById(id);
        if (trip.getPaymentStatus().equals(PaymentStatus.PAID)) {
            trip.setTripStatus(TripStatus.COMPLETED);
            repository.save(trip);
            log.debug("Trip status {} has been set to trip {}", TripStatus.COMPLETED, id);
        } else {
            String message = "Passenger hasn't paid for trip yet. Ask for payment!";
            log.debug("Trip status {} hasn't been set to trip {}. Cause: {}", TripStatus.COMPLETED, id, message);
            throw new PaymentException(message);
        }
    }

    public Page<TripDtoShortInfo> getAllNew(CarClass carClass, Pageable pageable) {
        log.trace("getAllNew: Page<TripDtoShortInfo>");
        return repository.findAllNewByCarClass(carClass, pageable);
    }


    public void saveDriverAndTaxiLocation(Trip trip, User user, Address taxiLocationAddress) {
        trip.setDriver(user);
        try {
            if (isAddressAssigned(taxiLocationAddress)) {
                trip.setTaxiLocationAddress(addressService.arrangeAddress(taxiLocationAddress));
                googleService.setTaxiArrivalTime(trip);
            }
            trip.setTripStatus(TripStatus.OFFERED);
        } catch (Exception e) {
            log.warn("Something wrong in setting taxi location address.");
        }
        repository.save(trip);
    }


    public Page<TripDtoForDriverPage> getAllByDriver(User user, Pageable pageable) {
        log.trace("getAllByDriver: Page<TripDtoForDriverPage>");
        Page<TripDtoForDriverPage> trips = repository.findAllByDriver(user, pageable);
        setPassengersAge(trips);
        replaceMetersToKilometers(trips);
        return trips;
    }


    public Page<TripDtoForDriverPage> getActiveByDriver(User user, Pageable pageable) {
        log.trace("getActiveByDriver: Page<TripDtoForDriverPage>");
        Page<TripDtoForDriverPage> trips = repository.findActiveByDriver(user, pageable);
        setPassengersAge(trips);
        replaceMetersToKilometers(trips);
        return trips;
    }

    public Page<TripDtoForDriverPage> getPastByDriver(User user, Pageable pageable) {
        log.trace("getPastByDriver: Page<TripDtoForDriverPage>");
        Page<TripDtoForDriverPage> trips = repository.findPastByDriver(user, pageable);
        setPassengersAge(trips);
        replaceMetersToKilometers(trips);
        return trips;
    }

    public TripDtoForDriverPage getDtoForDriverById(String id) {
        log.trace("getDtoForDriverById: {}", id);
        Optional<TripDtoForDriverPage> tripOptional = repository.findDtoForDriverById(id);
        TripDtoForDriverPage trip = tripOptional.orElseThrow(() -> {
            String message = "Such trip has not been found. Trip id =" + id;
            log.debug(message);
            return new IllegalArgumentException(message);
        });
        int passengerAge = getAgeFromBirthData(trip.getPassengerBirthDate());
        trip.setPassengerAge(passengerAge);
        replaceMetersToKilometers(trip);
        setDriversFunds(trip);
        return trip;
    }


    private void setDriversFunds(TripDtoForDriverPage trip) {
        BigDecimal tripPrice = trip.getPrice();
        Tariffs tariffs = paymentService.getTariffByCarClass(trip.getCarClass());
        BigDecimal driverFunds = paymentService.getDriverPart(tripPrice, tariffs);
        trip.setDriverFunds(driverFunds);
    }

    private void setPassengersAge(Page<TripDtoForDriverPage> trips) {
        for (TripDtoForDriverPage trip : trips) {
            int passengerAge = getAgeFromBirthData(trip.getPassengerBirthDate());
            trip.setPassengerAge(passengerAge);
        }
    }

    private int getAgeFromBirthData(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }


    private void replaceMetersToKilometers(Page<TripDtoForDriverPage> trips) {
        for (TripDtoForDriverPage trip : trips) {
            replaceMetersToKilometers(trip);
        }
    }

    private void replaceMetersToKilometers(TripDtoForDriverPage trip) {
        String distanceInMeters = trip.getDistance();
        String distanceInKiloMeters = formatInKilometers(distanceInMeters);
        trip.setDistance(distanceInKiloMeters);
    }

    private String formatInKilometers(String meters) {
        double distanceInMeters = Double.parseDouble(meters);
        double distanceInKilometers = distanceInMeters / 1000;
        String distance = Double.toString(distanceInKilometers);
        distance = distance.replaceAll("\\.", ",");
        int comaPlace = distance.indexOf(',');
        int symbolsAfterComa = distance.substring(comaPlace).length();
        if (symbolsAfterComa > 2) {
            distance = distance.substring(0, (comaPlace + 3));
        }
        return distance;
    }

    public String getNextActionDependingOnStatus(TripStatus status) {
        switch (status) {
            case OFFERED -> {
                return "start";
            }
            case DRIVING -> {
                return "complete";
            }
            default -> {
                return null;
            }
        }
    }

    private boolean isAddressAssigned(Address taxiLocationAddress) {
        return taxiLocationAddress != null && !taxiLocationAddress.getAddress().isBlank();
    }
}
