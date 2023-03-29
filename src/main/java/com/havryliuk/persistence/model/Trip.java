package com.havryliuk.persistence.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.google.maps.model.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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
    @JoinColumn(name = "departure_address")
    private Address originAddress;

    @NotNull
    @Valid
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "arrival_address")
    private Address destinationAddress;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private User passenger;

    private TripStatus tripStatus;

    private PaymentStatus paymentStatus;

    private CarClass carClass;

    private Duration approximateTravelDuration;

}
