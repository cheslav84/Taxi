package com.havryliuk.service;

import com.havryliuk.exceptions.UserAlreadyExistException;
import com.havryliuk.model.Driver;
import com.havryliuk.model.Passenger;
import com.havryliuk.model.User;
import com.havryliuk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, UserResource {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public Optional<User> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Override
    public void save(User user) {
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password is incorrect");
        }
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);//todo set false if implements emailing
        user.setRegistrationDate(LocalDateTime.now());
        setInitialBalanceIfNotManager(user);
        repository.save(user);
    }


    public void updateBalance(User user, BigDecimal rechargeValue) {
        setNewBalanceIfNotManager(user, rechargeValue);
        repository.save(user);
    }

    private void setNewBalanceIfNotManager(User user, BigDecimal rechargeValue) {
        switch (user.getRole()) {
            case PASSENGER -> {
                BigDecimal balance = ((Passenger) user).getBalance();
                balance = balance.add(rechargeValue);
                ((Passenger) user).setBalance(balance);
            }
            case DRIVER -> {
                BigDecimal balance = ((Driver) user).getBalance();
                balance = balance.add(rechargeValue);
                ((Driver) user).setBalance(balance);
            }
        }
    }


    private void setInitialBalanceIfNotManager(User user) {
        switch (user.getRole()) {
            case PASSENGER -> ((Passenger) user).setBalance(BigDecimal.ZERO);
            case DRIVER -> ((Driver) user).setBalance(BigDecimal.ZERO);
        }
    }


}