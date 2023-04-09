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
public class Passenger extends User {

    private BigDecimal balance;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Trip> trips;

}
