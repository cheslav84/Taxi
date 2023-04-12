package com.havryliuk.service;

import com.havryliuk.dto.trips.TripDtoForDriverPage;
import com.havryliuk.dto.trips.TripDtoShortInfo;
import com.havryliuk.dto.trips.TripDtoForDriverDetailed;
import com.havryliuk.dto.trips.TripDtoForPassengerPage;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.repository.TripRepository;
import com.havryliuk.util.google.map.GoogleService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Slf4j
@Service
public class TripService {

    private final AddressService addressService;
    private final GoogleService googleService;
    private final TripRepository repository;

    private final PaymentService paymentService;
//
//    private final TariffsRepository tariffsRepository;


    @Autowired
    public TripService(AddressService addressService, GoogleService googleService,
                       TripRepository repository, PaymentService paymentService) {
        this.addressService = addressService;
        this.googleService = googleService;
        this.repository = repository;
        this.paymentService = paymentService; // todo move to payment service
    }

    public Trip getById (String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Such trip hasn't been found"));
    }

    @Transactional
    public void payForTrip(User user, String tripId) throws PaymentException {
        Trip trip = getById(tripId);
        CompanyBalance companyBalance = paymentService.getCompanyBalance();
        paymentService.setPaymentPrices(user, trip, companyBalance);
        trip.setPaymentStatus(PaymentStatus.PAID);
        paymentService.saveCompanyBalance(companyBalance);
        repository.save(trip);
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


    public Page<TripDtoForPassengerPage> getAllByPassenger(User user, Pageable pageable) {
        Page<TripDtoForPassengerPage> tripsPage = repository.findAllByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForPassengerPage> getActiveByPassenger(User user, Pageable pageable) {
        Page<TripDtoForPassengerPage> tripsPage = repository.findActiveByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForPassengerPage> getPastByPassenger(User user, Pageable pageable) {
        Page<TripDtoForPassengerPage> tripsPage = repository.findPastByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoShortInfo> getAllNew(CarClass carClass, Pageable pageable) {
        return repository.findAllNewByCarClass(carClass, pageable);
    }


    private void setTaxiArrivalTimeReadable(Page<TripDtoForPassengerPage> trips) {
        for (TripDtoForPassengerPage trip : trips) {
            setTaxiArrivalTimeReadable(trip);
        }
    }

    private void setTaxiArrivalTimeReadable(TripDtoForPassengerPage trip) {
        String messageIfTimeNotDefined = "Time not defined";
        String time = trip. getTimeToTaxiArrivalInSeconds();
            if (time.equals("0")) {
                time = messageIfTimeNotDefined;
            } else {
                time = formatTime(time);
            }
            trip.setTimeToTaxiArrivalInSeconds(time);
    }


    private String formatTime(String time) {
        int seconds = Integer.parseInt(time);
        int hours = seconds / 3600 % 24;
        int minutes = seconds / 60 % 60;
        String hourSign = "hrs";
        String minuteSign = "min";
        StringBuilder stringBuilder = new StringBuilder();
        if (hours != 0) {
            stringBuilder.append(hours).append(" ").append(hourSign).append(" ");
        }
        if (minutes != 0) {
            stringBuilder.append(minutes).append(" ").append(minuteSign).append(" ");
        }
        if (hours == 0 && minutes == 0) {
            stringBuilder.append("1 ").append(minuteSign);
        }
        return stringBuilder.toString();
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
                () -> new IllegalArgumentException("Such trip has not been found. Trip id =" + id));
        int passengerAge = getAgeFromBirthData(trip.getPassengerBirthDate());
        trip.setPassengerAge(passengerAge);
        replaceMetersToKilometers(trip);
        setDriversFunds(trip);
        return trip;
    }


    public TripDtoForPassengerPage getDtoFoPassengerById(String id) {
        Optional<TripDtoForPassengerPage> tripOptional = repository.findDtoForPassengerById(id);
        TripDtoForPassengerPage trip = tripOptional.orElseThrow(
                () -> new IllegalArgumentException("Such trip has not been found. Trip id =" + id));
        setTaxiArrivalTimeReadable(trip);
        return trip;
    }

    private void setDriversFunds(TripDtoForDriverPage trip) {
        BigDecimal tripPrice = trip.getPrice();
        Tariffs tariffs = paymentService.getTariffByCarClass(trip.getCarClass());
        BigDecimal driverFunds = paymentService.getDriverPart(tripPrice, tariffs);
        trip.setDriverFunds(driverFunds);
    }

    @NotNull
    private BigDecimal getBigDecimal(TripDtoForDriverPage trip, Tariffs tariffs) {
        BigDecimal driverPartInPercent = BigDecimal.valueOf(tariffs.getDriverPartInPercent());
        BigDecimal price = trip.getPrice();
        return price
                .multiply(driverPartInPercent)
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
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
