//package com.havryliuk.repository;
//
//import com.havryliuk.dto.trips.TripDtoForPassenger;
//import com.havryliuk.model.*;
//import org.hibernate.criterion.ProjectionList;
//import org.hibernate.criterion.Projections;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.criteria.*;
//import java.util.List;
//
//
//@Repository
//public class TripRepositoryCustomImpl implements TripRepositoryCustom {
//
//    private final EntityManager entityManager;
//
//    @Autowired
//    public TripRepositoryCustomImpl(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }
//
//    @Override
//    public Page<TripDtoForPassenger> findAllByPassenger(User user, Pageable pageable) {
//
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<TripDtoForPassenger> criteriaQuery = criteriaBuilder.createQuery(TripDtoForPassenger.class);
//        Root<Trip> trip = criteriaQuery.from(Trip.class);
//
//        Join<Trip, Address> originAddress = trip.join("originAddress", JoinType.INNER);
//        Join<Trip, Address> destinationAddress = trip.join("destinationAddress", JoinType.INNER);
//
//
//        Join<Trip, Driver> driver = trip.join("driver", JoinType.LEFT);
//
////        Join<Driver, Car> car = driver.join("car", JoinType.LEFT);
//
//
//
//
////        Join<Trip, Address> originAddress = trip.join("originAddress", JoinType.INNER);
////        Join<Trip, Address> destinationAddress = trip.join("destinationAddress", JoinType.INNER);
////        Join<Trip, Driver> driver = trip.join("driver", JoinType.LEFT);
////        Join<Driver, Car> car = driver.join("car", JoinType.LEFT);
//
////        Join<Trip, User> userStr = trip.join("driver", JoinType.LEFT);
//
//
//
////        final ProjectionList projectionList= Projections.projectionList();
////        projectionList.add(Projections.property("trip.id"), "id");
////        projectionList.add(Projections.property("trip.departureDateTime"), "departureDateTime");
////        projectionList.add(Projections.property("trip.driverNameAndPhone"), "driverNameAndPhone");
////
////
////        criteriaQuery.
////        criteriaQuery.add(Restrictions.eq("class", "HIRING_MANAGER"));
//
//        Expression<String> driverNameAndPhone = criteriaBuilder.concat(
//                criteriaBuilder.concat(driver.get("name"), ", "), driver.get("phone"));
//
////        Expression<String> carStr = criteriaBuilder.concat(
////                criteriaBuilder.concat(car.get("brand"), " "), car.get("model"));
//
////        d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
//
//        criteriaQuery.multiselect(
//                trip.get("id"),
//                trip.get("departureDateTime"),
//                originAddress.get("address"),
//                destinationAddress.get("address"),
//                driverNameAndPhone,
//                driver.get("car")
////                driver.get("departureDateTime")
////                car.get("model")
//
////                carStr
////                driver.get("name", "phone").,
////                driver.get("driverNameAndPhone")
////                driver.get("phone")
//        );
//
////        criteriaQuery.select(
////                trip.get("id"),
////                trip.get("departureDateTime"),
//////                trip.get("originAddress.address"),
//////                trip.get("destinationAddress.address"),
////                driver.get("name")
//////                driver.get("name", "phone").,
//////                driver.get("driverNameAndPhone")
//////                driver.get("phone")
////        );
////
//
//
//
//
//        List<TripDtoForPassenger> resultList = entityManager.createQuery(criteriaQuery).getResultList();
//
//        int totalRows = resultList.size();
//
//        Page<TripDtoForPassenger> result = new PageImpl<>(resultList, pageable, totalRows);
//
//        return result;
//
//
//
////        return null;
//    }
//
//
////    @Query(value = """
////            select new com.havryliuk.dto.trips.TripDtoForPassenger
////                (t.id, t.departureDateTime, t.originAddress.address, t.destinationAddress.address,
////                d.name || ', ' || d.phone AS driverNameAndPhone,
////                d.car.brand || ' ' || d.car.model || ' ' || d.car.number AS car,
////                cast(t.timeToTaxiArrivalInSeconds as string),
////                t.paymentStatus, t.price)
////            from Trip t
////            left join t.driver d
////            left join d.car
////            where t.passenger = :user
////            order by t.departureDateTime desc
////            """
//////            , countQuery = "select count(id) from trip t where t.passenger = :user",
//////            nativeQuery = true
////    )
////    public Page<TripDtoForPassenger> findAllByPassenger(@Param("user")User user, Pageable pageable);
//
//}