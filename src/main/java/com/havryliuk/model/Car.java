package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Entity
public class Car {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    @Column(unique=true)
    private String number;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CarClass carClass;


}
