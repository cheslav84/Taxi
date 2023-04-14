package com.havryliuk.repository;

import com.havryliuk.dto.trips.*;
import com.havryliuk.model.CarClass;
import com.havryliuk.model.Trip;
import com.havryliuk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TripRepository extends PagingAndSortingRepository<Trip, String> {

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassengerPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price, t.tripStatus)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            """
    )
    Page<TripDtoForPassengerPage> findAllByPassenger(@Param("user") User user, Pageable pageable);

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassengerPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price, t.tripStatus)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            and (t.tripStatus != 'COMPLETED' and t.tripStatus != 'CANCELED')
            """
    )
    Page<TripDtoForPassengerPage> findActiveByPassenger(@Param("user") User user, Pageable pageable);//todo виключити з пошуку поїздки дата яких минула

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassengerPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price, t.tripStatus)
            from Trip t
            left join t.driver d
            left join d.car
            where t.passenger = :user
            and t.tripStatus = 'COMPLETED'
            """
    )
    Page<TripDtoForPassengerPage> findPastByPassenger(@Param("user") User user, Pageable pageable);


    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassengerPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                d.name || ', ' || d.phone AS driverNameAndPhone,
                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
                cast(t.timeToTaxiArrivalInSeconds as string),
                t.paymentStatus, t.price, t.tripStatus)
            from Trip t
            left join t.driver d
            left join d.car
            where t.id = :id
            """
    )
    Optional<TripDtoForPassengerPage> findDtoForPassengerById(@Param("id") String id);



    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoShortInfo
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address, t.price)
            from Trip t
            where t.tripStatus = 'NEW'
            and
            t.carClass = :carClass
            """
    )
    Page<TripDtoShortInfo> findAllNewByCarClass(@Param("carClass") CarClass carClass, Pageable pageable);//todo виключити з пошуку поїздки дата яких минула


    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForPassengerUpdate
                (t.id, t.departureDateTime, t.originAddress.address,
                 t.destinationAddress.address, t.price, t.carClass)
            from Trip t
            where t.id = :id
            """
    )
    Optional<TripDtoForPassengerUpdate> findTripDtoForUserUpdateById(@Param("id") String ide);

    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForDriverDetailed
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                 t.price, p.name, p.birthDate)
            from Trip t
            join t.passenger p
            where t.id = :id
            """
    )
    Optional<TripDtoForDriverDetailed> findDetailedDtoById(@Param("id") String id);


    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForDriverPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                p.name || ', ' || p.phone AS passengerNameAndPhone,
                p.birthDate, t.paymentStatus, t.tripStatus, t.price, t.carClass,
                cast(t.distanceInMeters as string))
            from Trip t
            join t.passenger p
            where t.driver = :user
            """
    )
    Page<TripDtoForDriverPage> findAllByDriver(@Param("user") User user, Pageable pageable);



    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForDriverPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                p.name || ', ' || p.phone AS passengerNameAndPhone,
                p.birthDate, t.paymentStatus, t.tripStatus, t.price, t.carClass,
                cast(t.distanceInMeters as string))
            from Trip t
            join t.passenger p
            where t.driver = :user
            and (t.tripStatus = 'OFFERED' or t.tripStatus = 'DRIVING')
            """
    )
    Page<TripDtoForDriverPage> findActiveByDriver(@Param("user") User user, Pageable pageable);


    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForDriverPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                p.name || ', ' || p.phone AS passengerNameAndPhone,
                p.birthDate, t.paymentStatus, t.tripStatus, t.price, t.carClass,
                cast(t.distanceInMeters as string))
            from Trip t
            join t.passenger p
            where t.driver = :user
            and t.tripStatus = 'COMPLETED'
            """
    )
    Page<TripDtoForDriverPage> findPastByDriver(@Param("user") User user, Pageable pageable);


    @Query(value = """
            select new com.havryliuk.dto.trips.TripDtoForDriverPage
                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
                p.name || ', ' || p.phone AS passengerNameAndPhone,
                p.birthDate, t.paymentStatus, t.tripStatus, t.price, t.carClass,
                cast(t.distanceInMeters as string))
            from Trip t
            join t.passenger p
            where t.id = :id
            """
    )
    Optional<TripDtoForDriverPage> findDtoForDriverById(@Param("id") String id);



}
