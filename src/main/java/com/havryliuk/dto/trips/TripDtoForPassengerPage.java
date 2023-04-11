package com.havryliuk.dto.trips;


import com.havryliuk.model.PaymentStatus;
import com.havryliuk.model.TripStatus;
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
public class TripDtoForPassengerPage extends TripDtoShortInfo {

    private String driverNameAndPhone;

    private String car;

    private String timeToTaxiArrivalInSeconds;

    private PaymentStatus paymentStatus;
    private TripStatus tripStatus;


    public TripDtoForPassengerPage(String id, LocalDateTime departureDateTime, String originAddress,
                                   String destinationAddress, String driverNameAndPhone,
                                   String car, String timeToTaxiArrivalInSeconds, PaymentStatus paymentStatus,
                                   BigDecimal price, TripStatus tripStatus) {
        super(id, departureDateTime, originAddress, destinationAddress, price);
        this.driverNameAndPhone = driverNameAndPhone;
        this.car = car;
        this.timeToTaxiArrivalInSeconds = timeToTaxiArrivalInSeconds;
        this.paymentStatus = paymentStatus;
        this.tripStatus = tripStatus;
    }
}
