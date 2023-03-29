package com.havryliuk.service;

import com.havryliuk.dto.UserDTO;
import com.havryliuk.persistence.model.*;
import com.havryliuk.util.factories.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserFactory factory;

    @Autowired
    public RegistrationService(UserFactory factory) {
        this.factory = factory;
    }

    public User registerUser(UserDTO userDTO) {
        User user = factory.createUser(userDTO);
        user.setUserStatus(UserStatus.NEW);
        return user;
    }


}
