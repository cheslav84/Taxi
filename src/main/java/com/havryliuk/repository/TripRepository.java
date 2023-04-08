package com.havryliuk.repository;

import com.havryliuk.dto.trips.TripDtoForDriver;
import com.havryliuk.dto.trips.TripDtoForPassenger;
import com.havryliuk.model.CarClass;
import com.havryliuk.model.Trip;
import com.havryliuk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
//public interface TripRepository extends PagingAndSortingRepository<Trip, String>, TripRepositoryCustom {
public interface TripRepository extends PagingAndSortingRepository<Trip, String> {

//    Page<TripDtoForPassenger> findAllByPassenger(@Param("user") User user, Pageable pageable);

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassenger
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            order by t.departureDateTime desc
            """
    )
    Page<TripDtoForPassenger> findAllByPassenger(@Param("user") User user, Pageable pageable);


    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassenger
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            and (t.tripStatus != 'COMPLETED' and t.tripStatus != 'CANCELED')
            order by t.departureDateTime desc
            """
    )
    Page<TripDtoForPassenger> findActiveByPassenger(@Param("user") User user, Pageable pageable);

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassenger
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            and t.tripStatus = 'COMPLETED'
            order by t.departureDateTime desc
            """
    )
    Page<TripDtoForPassenger> findPastByPassenger(@Param("user") User user, Pageable pageable);

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForDriver
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address, t.price)
            from Trip t
            where t.tripStatus = 'NEW'
            and
            t.carClass = :carClass
            order by t.departureDateTime desc
            """
    )
    Page<TripDtoForDriver> findAllNewByCarClass(@Param("carClass") CarClass carClass, Pageable pageable);


}
