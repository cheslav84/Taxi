package com.havryliuk.service;

import com.havryliuk.dto.trips.TripDtoForPassenger;
import com.havryliuk.model.*;
import com.havryliuk.repository.TariffsRepository;
import com.havryliuk.repository.TripRepository;
import com.havryliuk.util.google.map.GoogleService;
//import com.havryliuk.util.mappers.MapStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

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

    public String save(Trip trip, User user) {
        googleService.setAddressLocation(trip.getOriginAddress());
        googleService.setAddressLocation(trip.getDestinationAddress());
        googleService.setDistanceAndDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAYED);
        setPrice(trip);
        trip.setPassenger(user);
//        List<Trip> trips = ((Passenger) user).getTrips();
//        trips.add(trip);
        return repository.save(trip).getId();
//        return null;
    }

    private void setPrice(Trip trip) {//todo think of move to separate class ()
        Tariffs tariff = tariffsRepository.getTariffByCarClass(trip.getCarClass());
        BigDecimal pricePerKilometer = tariff.getPricePerKilometer();
        long distanceInMeters = trip.getDistanceInMeters();
        BigDecimal distance = BigDecimal.valueOf(distanceInMeters);
//        MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
        BigDecimal distanceInKilometers = distance.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
//        BigDecimal price = pricePerKilometer.multiply(distanceInKilometers, new MathContext(2, RoundingMode.HALF_UP));
        BigDecimal price = pricePerKilometer.multiply(distanceInKilometers);

        System.err.println("pricePerKilometer " + pricePerKilometer);
        System.err.println("distanceInMeters " + distanceInMeters);
        System.err.println("distance " + distance);
        System.err.println("distanceInKilometers " + distanceInKilometers);
        System.err.println("price " + price);

        trip.setPrice(price);
    }

    //    public List<TripDto> getByUser(User user) {
    public List<TripDtoForPassenger> getByUser(User user) {
//        UserTripDto userTripDto = structMapper.userToUserTripDto(user);

        System.err.println(user.getId());

//        return repository.findAllTrips();
        return repository.findAllByPassenger(user);
//        return repository.findAllByPassenger(userTripDto);

    }

//    public Optional<TripDTO> getByTripId(String id) {
//
//        return repository.findById(id);
//    }
}
