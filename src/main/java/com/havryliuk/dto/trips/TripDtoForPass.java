//package com.havryliuk.dto.trips;
//
//import com.havryliuk.model.Address;
//import com.havryliuk.model.PaymentStatus;
//import com.havryliuk.model.TripStatus;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//public interface TripDtoForPass {
//
//    @Value("#{target.id}")
//    String getId();
//
//    @Value("#{target.passenger}")
//    PassengerDto getPassenger();
//
//    @Value("#{target.driver}")
//    DriverDto getDriver();
//
//    LocalDateTime getDepartureDateTime();
//
//    Address getOriginAddress();
//
//    Address getDestinationAddress();
//
//    TripStatus getTripStatus();
//
//    PaymentStatus getPaymentStatus();
//
//    BigDecimal getPrice();
//
//}
