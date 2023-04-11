package com.havryliuk.dto.trips;


import com.havryliuk.model.Address;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TripDtoForDriverDetailed extends TripDtoShortInfo {

    private String passengerName;

    private LocalDate passengerBirthDate;

    private Address taxiLocationAddress;

    private int passengerAge;

    public TripDtoForDriverDetailed(String id, LocalDateTime departureDateTime,
                                    String originAddress, String destinationAddress,
                                    BigDecimal price, String passengerName,
                                    LocalDate passengerBirthDate) {

        super(id, departureDateTime, originAddress, destinationAddress, price);
        this.passengerName = passengerName;
        this.passengerBirthDate = passengerBirthDate;
        this.taxiLocationAddress = new Address();
    }
}
