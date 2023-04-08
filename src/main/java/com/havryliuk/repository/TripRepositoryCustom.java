//package com.havryliuk.repository;
//
//import com.havryliuk.dto.trips.TripDtoForPassenger;
//import com.havryliuk.model.Trip;
//import com.havryliuk.model.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//
//@Repository
//public interface TripRepositoryCustom {
//
//    Page<TripDtoForPassenger> findAllByPassenger(@Param("user") User user, Pageable pageable);
//
//
//}