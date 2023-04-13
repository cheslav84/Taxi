package com.havryliuk.service.tripService;

import com.havryliuk.dto.trips.*;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.repository.TripRepository;
import com.havryliuk.service.AddressService;
import com.havryliuk.service.PaymentService;
import com.havryliuk.util.google.map.GoogleService;
import com.havryliuk.util.mappers.TripUpdateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Slf4j
@Service
public class PassengerTripService {

    private final AddressService addressService;
    private final GoogleService googleService;
    private final TripRepository repository;
    private final PaymentService paymentService;
    private final TripUpdateMapper tripUpdateMapper;


    @Autowired
    public PassengerTripService(AddressService addressService, GoogleService googleService,
                                TripRepository repository, PaymentService paymentService,
                                TripUpdateMapper tripUpdateMapper) {
        this.addressService = addressService;
        this.googleService = googleService;
        this.repository = repository;
        this.paymentService = paymentService;
        this.tripUpdateMapper = tripUpdateMapper;
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

    public TripDtoForPassengerUpdate getTripDtoForUserUpdateById(String id) {
        return repository.findTripDtoForUserUpdateById(id)
                .orElseThrow(() -> new IllegalArgumentException("Such trip hasn't been found"));
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



    private void setPrice(Trip trip) {//todo think of move to separate class ()
        Tariffs tariff = paymentService.getTariffByCarClass(trip.getCarClass());
        BigDecimal pricePerKilometer = tariff.getPricePerKilometer();
        long distanceInMeters = trip.getDistanceInMeters();
        BigDecimal distance = BigDecimal.valueOf(distanceInMeters);
        BigDecimal distanceInKilometers = distance.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        BigDecimal price = pricePerKilometer.multiply(distanceInKilometers);
        trip.setPrice(price);
    }

    public void deleteTrip(String id) {
        Trip trip = getById(id);
        if (trip.getTripStatus().equals(TripStatus.NEW) || trip.getTripStatus().equals(TripStatus.OFFERED)) {
            repository.delete(trip);
        } else {
            throw new UnsupportedOperationException("Finished or driving trip can't be canceled.");
        }
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


    public TripDtoForPassengerPage getDtoFoPassengerById(String id) {
        Optional<TripDtoForPassengerPage> tripOptional = repository.findDtoForPassengerById(id);
        TripDtoForPassengerPage trip = tripOptional.orElseThrow(
                () -> new IllegalArgumentException("Such trip has not been found. Trip id =" + id));
        setTaxiArrivalTimeReadable(trip);
        return trip;
    }

    public void updateTrip(TripDtoForPassengerUpdate tripDto) {
        Trip trip = getById(tripDto.getId());
        if (tripStatusAllowsUpdateTrip(trip.getTripStatus())) {
            tripUpdateMapper.setUpdatedValues(trip, tripDto);
            repository.save(trip);
        } else {
            String message = "Finished or driving trip can't be updated.";
            log.info(message);
            throw new UnsupportedOperationException(message);
        }
    }

    private boolean tripStatusAllowsUpdateTrip(TripStatus tripStatus) {
        return tripStatus.equals(TripStatus.NEW) || tripStatus.equals(TripStatus.OFFERED);
    }
}
