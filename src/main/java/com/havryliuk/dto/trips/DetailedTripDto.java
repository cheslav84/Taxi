//package com.havryliuk.dto.trips;
//
//import com.havryliuk.model.*;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//public interface DetailedTripDto {
//
//    @Value("#{target.id}")
//    String getId();
//
//    @Value("#{target.passenger}")
//    PassengerDto getPassenger();
//
//    @Value("#{target.driver}")
//    DriverDto getDriver();
//    CarClass getCarClass();
//
//    LocalDateTime getDepartureDateTime();
//
//    Address getOriginAddress();
//
//    Address getDestinationAddress();
//
//    Address getTaxiLocationAddress();
//
//    TripStatus getTripStatus();
//
//    PaymentStatus getPaymentStatus();
//
//    BigDecimal getPrice();
//
//    long getDistanceInMeters();
//
//    long getDurationInSeconds();
//
//    long getTimeToTaxiArrivalInSeconds();
//}
