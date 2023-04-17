package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Driver extends User {

    private BigDecimal balance;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Trip> trips;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    private Car car;


}
