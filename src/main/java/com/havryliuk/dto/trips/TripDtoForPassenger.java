package com.havryliuk.dto.trips;


import com.havryliuk.model.Car;
import com.havryliuk.model.PaymentStatus;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//public record TripDtoForPassenger(
// String id,
// LocalDateTime departureDateTime,
// String originAddress,
// String destinationAddress,
// String driverNameAndPhone,
// String car,
// String timeToTaxiArrivalInSeconds,
// PaymentStatus paymentStatus,//todo display price in different colors
// BigDecimal price
//) {}

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TripDtoForPassenger implements TripDtoForUser {

    private String id;

    private LocalDateTime departureDateTime;

    private String originAddress;

    private String destinationAddress;

//    @Formula(value = "concat(f_name, f_phone)")
    private String driverNameAndPhone;
//    private String driverName;

    private String car;

    private String timeToTaxiArrivalInSeconds;

    private PaymentStatus paymentStatus;//todo display price in different colors

    private BigDecimal price;
}