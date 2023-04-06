package com.havryliuk.dto.trips;

import com.havryliuk.model.Address;
import com.havryliuk.model.Car;
import com.havryliuk.model.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//public record TripDtoForPassenger(
//        String id,
//        LocalDateTime departureDateTime,
//        Address originAddress,
//        Address destinationAddress,
//        String driverId,
//        String driverName,
//        String driverPhone,
//        Car car,
//long timeToTaxiArrivalInSeconds;
//        PaymentStatus paymentStatus,
//        BigDecimal price
//) {}

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TripDtoForPassenger {

    private String id;

    private LocalDateTime departureDateTime;

    private Address originAddress;

    private Address destinationAddress;

    private String driverId;

    private String driverName;

    private String driverPhone;

    private Car car;

    private long timeToTaxiArrivalInSeconds;

    private PaymentStatus paymentStatus;//todo display price in different colors

    private BigDecimal price;


}
