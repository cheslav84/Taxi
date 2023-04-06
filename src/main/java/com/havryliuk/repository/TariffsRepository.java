package com.havryliuk.repository;

import com.havryliuk.model.CarClass;
import com.havryliuk.model.Tariffs;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffsRepository extends CrudRepository<Tariffs, String> {
    Tariffs getTariffByCarClass(CarClass carClass);

}
