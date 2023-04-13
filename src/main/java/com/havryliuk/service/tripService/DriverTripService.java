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
import java.math.RoundingMode;
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

    public Trip getById (String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Such trip hasn't been found"));
    }


    public TripDtoForDriverDetailed getDtoById (String id) {
        TripDtoForDriverDetailed trip = repository.findDetailedDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Such trip hasn't been found"));
        int passengerAge = getAgeFromBirthData(trip.getPassengerBirthDate());
        trip.setPassengerAge(passengerAge);
        return trip;
    }


    public String save(Trip trip, User user) {
        trip.setOriginAddress(addressService.arrangeAddress(trip.getOriginAddress()));
        trip.setDestinationAddress(addressService.arrangeAddress(trip.getDestinationAddress()));
        googleService.setDistanceAndDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAID);
        setPrice(trip);
        trip.setPassenger(user);
        return repository.save(trip).getId();
    }

    public void setStatusDrivingById(String id) {
        Trip trip = getById(id);
        trip.setTripStatus(TripStatus.DRIVING);
        repository.save(trip);
    }

    public void setStatusCompletedById(String id) throws PaymentException {
        Trip trip = getById(id);
        if (trip.getPaymentStatus().equals(PaymentStatus.PAID)) {
            trip.setTripStatus(TripStatus.DRIVING);
            repository.save(trip);
        } else {
            throw new PaymentException("Passenger hasn't paid for trip yet. Ask for payment!");
        }
    }


    private void setPrice(Trip trip) {//todo think of move to separate class ()
        Tariffs tariff = paymentService.getTariffByCarClass(trip.getCarClass());
        BigDecimal pricePerKilometer = tariff.getPricePerKilometer();
        long distanceInMeters = trip.getDistanceInMeters();
        BigDecimal distance = BigDecimal.valueOf(distanceInMeters);
        BigDecimal distanceInKilometers = distance.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        BigDecimal price = pricePerKilometer.multiply(distanceInKilometers);
        trip.setPrice(price);
    }


    public Page<TripDtoShortInfo> getAllNew(CarClass carClass, Pageable pageable) {
        return repository.findAllNewByCarClass(carClass, pageable);
    }


    public void saveDriverAndTaxiLocation(Trip trip, User user, Address taxiLocationAddress) {
        trip.setDriver(user);
        try {
            trip.setTaxiLocationAddress(addressService.arrangeAddress(taxiLocationAddress));
            trip.setTripStatus(TripStatus.OFFERED);
            googleService.setTaxiArrivalTime(trip);
        } catch (Exception e) {
            log.warn("Something wrong in setting taxi location address.");
        }
        repository.save(trip);
    }

    public Page<TripDtoForDriverPage> getAllByDriver(User user, Pageable pageable) {
        Page<TripDtoForDriverPage> trips = repository.findAllByDriver(user, pageable);
        setPassengersAge(trips);
        replaceMetersToKilometers(trips);
        return trips;
    }



    public Page<TripDtoForDriverPage> getActiveByDriver(User user, Pageable pageable) {
        Page<TripDtoForDriverPage> trips = repository.findActiveByDriver(user, pageable);
        setPassengersAge(trips);
        replaceMetersToKilometers(trips);
        return trips;
    }

    public Page<TripDtoForDriverPage> getPastByDriver(User user, Pageable pageable) {
        Page<TripDtoForDriverPage> trips = repository.findPastByDriver(user, pageable);
        setPassengersAge(trips);
        replaceMetersToKilometers(trips);
        return trips;
    }

    public TripDtoForDriverPage getDtoForDriverById(String id) {
        Optional<TripDtoForDriverPage> tripOptional = repository.findDtoForDriverById(id);
        TripDtoForDriverPage trip = tripOptional.orElseThrow(
                () -> {
                    String message = "Such trip has not been found. Trip id =" + id;
                    log.warn(message);
                    return new IllegalArgumentException(message);
                }
        );
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
        for (TripDtoForDriverPage trip: trips) {
            int passengerAge = getAgeFromBirthData(trip.getPassengerBirthDate());
            trip.setPassengerAge(passengerAge);
        }
    }

    private int getAgeFromBirthData(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }


    private void replaceMetersToKilometers(Page<TripDtoForDriverPage> trips) {
        for (TripDtoForDriverPage trip: trips) {
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
            case OFFERED -> { return "start"; }
            case DRIVING -> { return "complete"; }
            default -> { return null; }
        }
    }

}
