package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class Trip {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "departure_address")
    private Address departureAddress;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "arrival_address")
    private Address arrivalAddress;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Person driver;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Person passenger;

    private TripStatus tripStatus;

    private PaymentStatus paymentStatus;

    private CarClass carClass;

}
