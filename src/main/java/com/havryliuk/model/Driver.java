package com.havryliuk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

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
    List<Trip> trips;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    Car car;


}
