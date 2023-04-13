package com.havryliuk.dto.trips;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TripDtoShortInfo {

    private String id;

    @NotNull(message="Choose the trip date please")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureDateTime;

    @NotNull
    @Valid
    private String originAddress;

    @NotNull
    @Valid
    private String destinationAddress;

    private BigDecimal price;
}
