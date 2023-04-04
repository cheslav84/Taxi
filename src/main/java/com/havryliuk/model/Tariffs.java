package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
public class Tariffs {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CarClass carClass;

    @NotNull
    @DecimalMin(value = "1.0")
    @DecimalMax(value = "100.0")
    @Digits(integer=3, fraction=2)
    private BigDecimal pricePerKilometer;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private int driverPartInPercent;

}
