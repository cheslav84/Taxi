package com.havryliuk.service;

import com.havryliuk.model.Tariffs;
import com.havryliuk.repository.TariffsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffsService {

    private final TariffsRepository repository;

    @Autowired
    public TariffsService(TariffsRepository repository) {
        this.repository = repository;
    }

    public Iterable<Tariffs> findAll () {
        return repository.findAll();
    }
}
