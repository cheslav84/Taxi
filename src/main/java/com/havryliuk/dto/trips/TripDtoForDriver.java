package com.havryliuk.dto.trips;


import com.havryliuk.model.CarClass;
import com.havryliuk.model.PaymentStatus;
import lombok.*;

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
public class TripDtoForDriver implements TripDtoForUser {

    private String id;

    private LocalDateTime departureDateTime;

    private String originAddress;

    private String destinationAddress;

    private BigDecimal price;
//    private CarClass carClass;
}
