package com.havryliuk.service;

import com.havryliuk.dto.trips.TripDtoForDriver;
import com.havryliuk.dto.trips.TripDtoForDriverDetailed;
import com.havryliuk.dto.trips.TripDtoForPassenger;
import com.havryliuk.model.*;
import com.havryliuk.repository.TariffsRepository;
import com.havryliuk.repository.TripRepository;
import com.havryliuk.util.google.map.GoogleService;
//import com.havryliuk.util.mappers.MapStructMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class TripService {
    private final GoogleService googleService;
    private final TripRepository repository;
    private final TariffsRepository tariffsRepository;
//    private final MapStructMapper structMapper;

    @Autowired
    public TripService(GoogleService googleService, TripRepository repository, TariffsRepository tariffsRepository) {
        this.googleService = googleService;
        this.repository = repository;
        this.tariffsRepository = tariffsRepository;
    }

//    @Autowired
//    public TripService(GoogleService googleService, TripRepository repository) {
//        this.googleService = googleService;
//        this.repository = repository;
//    }



    public Trip getById (String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Such trip hasn't been found"));
    }

    public TripDtoForDriverDetailed getDtoById (String id) {
        return repository.findDtoById(id).orElseThrow(() -> new IllegalArgumentException("Such trip hasn't been found"));
    }

    public String save(Trip trip, User user) {
        googleService.setAddressLocation(trip.getOriginAddress());
        googleService.setAddressLocation(trip.getDestinationAddress());
        googleService.setDistanceAndDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAYED);
        setPrice(trip);
        trip.setPassenger(user);
        return repository.save(trip).getId();
    }

    private void setPrice(Trip trip) {//todo think of move to separate class ()
        Tariffs tariff = tariffsRepository.getTariffByCarClass(trip.getCarClass());
        BigDecimal pricePerKilometer = tariff.getPricePerKilometer();
        long distanceInMeters = trip.getDistanceInMeters();
        BigDecimal distance = BigDecimal.valueOf(distanceInMeters);
        BigDecimal distanceInKilometers = distance.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        BigDecimal price = pricePerKilometer.multiply(distanceInKilometers);
        trip.setPrice(price);
    }


    public Page<TripDtoForPassenger> getAllByUser(User user, Pageable pageable) {
        Page<TripDtoForPassenger> tripsPage = repository.findAllByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForPassenger> getActiveByUser(User user, Pageable pageable) {
        Page<TripDtoForPassenger> tripsPage = repository.findActiveByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForPassenger> getPastByUser(User user, Pageable pageable) {
        Page<TripDtoForPassenger> tripsPage = repository.findPastByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForDriver> getAllNew(CarClass carClass, Pageable pageable) {
        return repository.findAllNewByCarClass(carClass, pageable);
    }



    private void setTaxiArrivalTimeReadable(Page<TripDtoForPassenger> trips) {
        String messageIfTimeNotDefined = "Time not defined";
        for (TripDtoForPassenger trip : trips) {
            String time = trip. getTimeToTaxiArrivalInSeconds();
            if (time.equals("0")) {
                time = messageIfTimeNotDefined;
            } else {
                time = formatTime(time);
            }
            trip.setTimeToTaxiArrivalInSeconds(time);
        }
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
            trip.setTaxiLocationAddress(taxiLocationAddress);
            googleService.setAddressLocation(taxiLocationAddress);
            googleService.setTaxiArrivalTime(trip);
        } catch (Exception e) {
            log.warn("Something wrong in setting taxi location address.");
        }
        repository.save(trip);
    }
}
