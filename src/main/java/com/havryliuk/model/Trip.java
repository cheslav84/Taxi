package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
//@ToString
@Entity
public class Trip {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull(message="Choose the trip date please")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureDateTime;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "origin_address")
    private Address originAddress;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "destination_address")
    private Address destinationAddress;

//    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "taxi_location_address")
    private Address taxiLocationAddress;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "passenger_id")
    private User passenger;

    @Enumerated(EnumType.STRING)
    private TripStatus tripStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private CarClass carClass;

    private BigDecimal price;

    private long distanceInMeters;

    private long durationInSeconds;

    private long timeToTaxiArrivalInSeconds;

}
