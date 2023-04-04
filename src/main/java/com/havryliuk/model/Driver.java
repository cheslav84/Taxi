package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Driver extends User {

    private BigDecimal balance;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Trip> trips;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    Car car;


}
