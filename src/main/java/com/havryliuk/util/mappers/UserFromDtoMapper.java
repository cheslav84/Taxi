package com.havryliuk.util.mappers;

import com.havryliuk.dto.UserDto;
import com.havryliuk.model.Driver;
import com.havryliuk.model.Manager;
import com.havryliuk.model.Passenger;
import com.havryliuk.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFromDtoMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserFromDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User map(UserDto userDTO) {
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
