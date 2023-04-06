package com.havryliuk.repository;

import com.havryliuk.dto.trips.TripDtoForPassenger;
import com.havryliuk.model.Trip;
import com.havryliuk.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends CrudRepository<Trip, String> {

    @Query("""
            select new com.havryliuk.dto.trips.TripDtoForPassenger
            (t.id, t.departureDateTime, t.originAddress, t.destinationAddress, d.id,
            d.name, d.phone, d.car,
            t.timeToTaxiArrivalInSeconds, t.paymentStatus, t.price)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            order by t.departureDateTime desc
            """)
    List<TripDtoForPassenger> findAllByPassenger(@Param("user")User user);

//    where p.id = :pass_id    join t.passenger p where t.passenger = :user

}

//    @Query("""
//            select new com.havryliuk.dto.trips.TripDtoForPassenger
//            (t.id, t.departureDateTime, t.originAddress, t.destinationAddress, d.id,
//            d.name, d.phone, d.car, t.paymentStatus, t.price)
//            from Trip t
//            join t.driver d
//            where t.passenger = :user
//            order by t.departureDateTime desc
//            """)
//    List<TripDtoForPassenger> findAllByPassenger(@Param("user")User user);

//    @Query("""
//            select new com.havryliuk.dto.trips.TripDtoForPassenger
//            (t.id, t.departureDateTime, t.originAddress, t.destinationAddress, t.paymentStatus, t.price)
//            from Trip t
//            where t.passenger = :user
//            order by t.departureDateTime desc
//            """)
//    List<TripDtoForPassenger> findAllByPassenger(@Param("user")User user);
