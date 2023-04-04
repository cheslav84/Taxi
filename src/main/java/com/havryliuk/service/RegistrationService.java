//package com.havryliuk.service;
//
//import com.havryliuk.dto.UserDTO;
//import com.havryliuk.exceptions.UserAlreadyExistException;
//import com.havryliuk.persistence.model.*;
//import com.havryliuk.persistence.repository.UserRepository;
//import com.havryliuk.util.factories.UserFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class RegistrationService {
//
//    private final UserFactory factory;
//    private final UserRepository repository;
//
//    @Autowired
//    public RegistrationService(UserFactory factory, UserRepository repository) {
//        this.factory = factory;
//        this.repository = repository;
//    }
//
//    public User registerUser(UserDTO userDTO) {
//        if (emailExists(userDTO.getEmail())) {
//            throw new UserAlreadyExistException("There is an account with that email address: "
//                    + userDTO.getEmail());
//        }
//
//        User user = factory.createUser(userDTO);
//        user.setEnabled(true);//todo set false if implements emailing
//        user.setRegistrationDate(LocalDateTime.now());
//        return repository.save(user);
//    }
//
//    private boolean emailExists(String email) {
//        return repository.findByEmail(email).isPresent();
//    }
//
//}
