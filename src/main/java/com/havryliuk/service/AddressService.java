package com.havryliuk.service;

import com.havryliuk.model.Address;
import com.havryliuk.repository.AddressRepository;
import com.havryliuk.util.google.map.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final GoogleService googleService;

    private final AddressRepository repository;

    @Autowired

    public AddressService(GoogleService googleService, AddressRepository repository) {
        this.googleService = googleService;
        this.repository = repository;
    }

    public Address arrangeAddress(Address originAddress) {
        return repository.findByAddress(originAddress.getAddress())
                .orElseGet(() -> {
                    googleService.setAddressLocation(originAddress);
                    return originAddress;
                });
    }
}
