package com.havryliuk.util.factories;

import com.havryliuk.dto.UserDTO;
import com.havryliuk.persistence.model.Driver;
import com.havryliuk.persistence.model.Manager;
import com.havryliuk.persistence.model.Passenger;
import com.havryliuk.persistence.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    private final ModelMapper modelMapper;

    @Autowired
    public UserFactory(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User createUser(UserDTO userDTO) {
        switch (userDTO.getRole()){
            case PASSENGER -> {
                return modelMapper.map(userDTO, Passenger.class);
            }
            case MANAGER -> {
                return modelMapper.map(userDTO, Manager.class);
            }
            case DRIVER -> {
                return modelMapper.map(userDTO, Driver.class);
            }
            default -> throw new IllegalArgumentException("User role is not defined.");
        }
    }

}
