package com.havryliuk.dto.trips;

import com.havryliuk.model.CarClass;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class TripDtoForPassengerUpdate extends TripDtoShortInfo {

    private CarClass carClass;

    public TripDtoForPassengerUpdate(String id, LocalDateTime departureDateTime,
                                     String originAddress, String destinationAddress,
                                     BigDecimal price, CarClass carClass) {

        super(id, departureDateTime, originAddress, destinationAddress, price);
        this.carClass = carClass;
    }
}
