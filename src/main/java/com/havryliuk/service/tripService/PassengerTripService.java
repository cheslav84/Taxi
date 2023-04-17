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

    public String save(Trip trip, User user) {
        log.trace("creating new trip, for user {}", user.getEmail());
        trip.setOriginAddress(addressService.arrangeAddress(trip.getOriginAddress()));
        trip.setDestinationAddress(addressService.arrangeAddress(trip.getDestinationAddress()));
        googleService.setDistanceAndDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAID);
        setPrice(trip);
        trip.setPassenger(user);
        return repository.save(trip).getId();
    }

    public Trip getById(String id) {
        log.trace("getById, id={}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.info("trip with id={} hasn't been found.", id);
            return new IllegalArgumentException("Such trip hasn't been found");
        });
    }

    public TripDtoForPassengerUpdate getTripDtoForUserUpdateById(String id) {
        return repository.findTripDtoForUserUpdateById(id)
                .orElseThrow(() -> {
                    log.info("trip with id={} hasn't been found.", id);
                    return new IllegalArgumentException("Such trip hasn't been found");
                });
    }


    public Page<TripDtoForPassengerPage> getAllByPassenger(User user, Pageable pageable) {
        log.trace("getAllByPassenger: Page<TripDtoForPassengerPage>");
        Page<TripDtoForPassengerPage> tripsPage = repository.findAllByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForPassengerPage> getActiveByPassenger(User user, Pageable pageable) {
        log.trace("getActiveByPassenger: Page<TripDtoForPassengerPage>");
        Page<TripDtoForPassengerPage> tripsPage = repository.findActiveByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public Page<TripDtoForPassengerPage> getPastByPassenger(User user, Pageable pageable) {
        log.trace("getPastByPassenger: Page<TripDtoForPassengerPage>");
        Page<TripDtoForPassengerPage> tripsPage = repository.findPastByPassenger(user, pageable);
        setTaxiArrivalTimeReadable(tripsPage);
        return tripsPage;
    }

    public TripDtoForPassengerPage getDtoFoPassengerById(String id) {
        log.trace("getDtoFoPassengerById: {}", id);
        Optional<TripDtoForPassengerPage> tripOptional = repository.findDtoForPassengerById(id);
        TripDtoForPassengerPage trip = tripOptional.orElseThrow(
                () -> new IllegalArgumentException("Such trip has not been found. Trip id =" + id));
        setTaxiArrivalTimeReadable(trip);
        return trip;
    }


    @Transactional
    public void payForTrip(User passenger, String tripId) throws PaymentException {
        log.trace("payForTrip, id={}", tripId);
        Trip trip = getById(tripId);
        CompanyBalance companyBalance = paymentService.getCompanyBalance();
        paymentService.setPaymentPrices(passenger, trip, companyBalance);
        trip.setPaymentStatus(PaymentStatus.PAID);
        paymentService.saveCompanyBalance(companyBalance);
        repository.save(trip);
    }


    public void updateTrip(TripDtoForPassengerUpdate tripDto) {
        log.trace("updateTrip, id={}", tripDto.getId());
        Trip trip = getById(tripDto.getId());
        if (tripStatusAllowsUpdateTrip(trip.getTripStatus())) {
            tripUpdateMapper.setUpdatedValues(trip, tripDto);
            setPrice(trip);
            repository.save(trip);
            log.debug("trip {} was successfully updated", trip.getId());
        } else {
            String message = "Finished or driving trip can't be updated.";
            log.info("trip {} hasn't been updated. Cause: {}", trip.getId(), message);
            throw new UnsupportedOperationException(message);
        }
    }

    public void deleteTrip(String id) {
        log.trace("deleting a trip with id={}", id);
        Trip trip = getById(id);
        if (trip.getTripStatus().equals(TripStatus.NEW) || trip.getTripStatus().equals(TripStatus.OFFERED)) {
            repository.delete(trip);
            log.info("trip with id={} has been successfully deleted.", id);
        } else {
            String message = "Finished or driving trip can't be canceled.";
            log.info("trip with id={} hasn't been deleted. Cause: {}", id, message);
            throw new UnsupportedOperationException(message);
        }
    }

    private void setPrice(Trip trip) {
        log.trace("setting trip price, for trip {}", trip.getId());
        Tariffs tariff = paymentService.getTariffByCarClass(trip.getCarClass());
        BigDecimal pricePerKilometer = tariff.getPricePerKilometer();
        long distanceInMeters = trip.getDistanceInMeters();
        BigDecimal distance = BigDecimal.valueOf(distanceInMeters);
        BigDecimal distanceInKilometers = distance.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        BigDecimal price = pricePerKilometer.multiply(distanceInKilometers);
        trip.setPrice(price);
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

    private boolean tripStatusAllowsUpdateTrip(TripStatus tripStatus) {
        return tripStatus.equals(TripStatus.NEW) || tripStatus.equals(TripStatus.OFFERED);
    }
}
