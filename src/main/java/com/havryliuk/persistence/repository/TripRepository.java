package com.havryliuk.persistence.repository;

import com.havryliuk.persistence.model.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, String> {

}
