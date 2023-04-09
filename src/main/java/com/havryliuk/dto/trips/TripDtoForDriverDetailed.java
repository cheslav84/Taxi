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
public class TripDtoForDriverDetailed implements TripDtoForUser {

    private String id;

    private LocalDateTime departureDateTime;

    private String originAddress;

    private String destinationAddress;

    private BigDecimal price;

    private String passengerName;

    private LocalDate passengerBirthDate;

    private Address taxiLocationAddress;

    public TripDtoForDriverDetailed(String id, LocalDateTime departureDateTime,
                                    String originAddress, String destinationAddress,
                                    BigDecimal price, String passengerName,
                                    LocalDate passengerBirthDate) {
        this.id = id;
        this.departureDateTime = departureDateTime;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.price = price;
        this.passengerName = passengerName;
        this.passengerBirthDate = passengerBirthDate;
        this.taxiLocationAddress = new Address();
    }
}
