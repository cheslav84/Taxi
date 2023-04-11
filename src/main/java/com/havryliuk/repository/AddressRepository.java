package com.havryliuk.repository;

import com.havryliuk.model.Address;
import com.havryliuk.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, String> {

    Optional<Address> findByAddress(String address);

}
