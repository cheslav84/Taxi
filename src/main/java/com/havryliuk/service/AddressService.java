package com.havryliuk.service;

import com.havryliuk.model.Address;
import com.havryliuk.repository.AddressRepository;
import com.havryliuk.util.google.map.GoogleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressService {

    private final GoogleService googleService;

    private final AddressRepository repository;

    @Autowired

    public AddressService(GoogleService googleService, AddressRepository repository) {
        this.googleService = googleService;
        this.repository = repository;
    }

    public Address arrangeAddress(Address address) {
        log.trace("arranging address {}", address.getAddress());
        return repository.findByAddress(address.getAddress())
                .orElseGet(() -> {
                    log.debug("address {} hasn't been found in database.", address.getAddress());
                    googleService.setAddressLocation(address);
                    return address;
                });
    }
}
