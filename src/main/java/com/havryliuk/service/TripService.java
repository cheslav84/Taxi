package com.havryliuk.service;

import com.havryliuk.model.Trip;
import com.havryliuk.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    private final TripRepository repository;

    @Autowired
    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    public String save(Trip trip) {
        return repository.save(trip).getId();
    }
}