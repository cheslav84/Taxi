package com.havryliuk.dto.trips;

import com.havryliuk.model.CarClass;
import com.havryliuk.model.PaymentStatus;
import com.havryliuk.model.TripStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TripDtoForDriverPage extends TripDtoShortInfo {
    private String passengerNameAndPhone;
    private LocalDate passengerBirthDate;
    private PaymentStatus paymentStatus;
    private TripStatus tripStatus;
    private CarClass carClass;
    private String distance;
    private int passengerAge;
    private BigDecimal driverFunds;


    public TripDtoForDriverPage(String id, LocalDateTime departureDateTime, String originAddress,
                                String destinationAddress, String passengerNameAndPhone,
                                LocalDate passengerBirthDate, PaymentStatus paymentStatus,
                                TripStatus tripStatus, BigDecimal price, CarClass carClass, String distance) {
        super(id, departureDateTime, originAddress, destinationAddress, price);
        this.passengerNameAndPhone = passengerNameAndPhone;
        this.passengerBirthDate = passengerBirthDate;
        this.paymentStatus = paymentStatus;
        this.tripStatus = tripStatus;
        this.carClass = carClass;
        this.distance = distance;
    }
}
