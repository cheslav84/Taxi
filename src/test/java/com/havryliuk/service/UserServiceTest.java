package com.havryliuk.service;

import com.havryliuk.exceptions.UserAlreadyExistException;
import com.havryliuk.model.Driver;
import com.havryliuk.model.Passenger;
import com.havryliuk.model.Role;
import com.havryliuk.model.User;
import com.havryliuk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private User user;


    @BeforeEach
    public void setupUser() {
        user =  new Passenger();
        user.setId("id");
        user.setEmail("email@com");
        user.setPassword("QWQWqwqw!1");
        user.setName("userName");
    }

    @Test
    void getByIdFound() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        User actual = userService.getById(user.getId());
        assertEquals(user, actual);
    }

    @Test
    void getByIdNotFound() {
        when(repository.findById(user.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.getById(user.getId()));
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void getByIdNull() {
        when(repository.findById(null)).thenReturn(Optional.empty());
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.getById(null));
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void loadUserByUsername() {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User actual = userService.loadUserByUsername(user.getEmail());
        assertEquals(user, actual);
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail())
        );
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void loadUserByUsernameNull() {
        when(repository.findByEmail(null)).thenReturn(Optional.empty());
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(null));
        assertEquals("User not found.", exception.getMessage());
    }


    @Test
    void saveUserWhenPasswordNull() {
        user.setPassword(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.save(user));
        assertEquals("Password is incorrect.", exception.getMessage());
    }

    @Test
    void saveUserWhenEmailIsPresent() {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userService.save(user));
        assertEquals("User already exists.", exception.getMessage());
    }


    @Test
    void saveUserAssertPasswordEncoded() {
        user.setRole(Role.PASSENGER);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        String rawPassword = user.getPassword();
        userService.save(user);
        assertNotEquals(user.getPassword(), rawPassword);
    }

    @Test
    void saveUserAssertRegistrationDateSet() {
        user.setRole(Role.PASSENGER);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        userService.save(user);
        assertNotNull(user.getRegistrationDate());
    }

    @Test
    void saveUserAssertPassengerBalanceSet() {
        user.setRole(Role.PASSENGER);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        userService.save(user);
        BigDecimal balance = ((Passenger) user).getBalance();
        assertEquals(balance, BigDecimal.ZERO);
    }

    @Test
    void saveUserAssertDriverBalanceSet() {
        User driver = new Driver();
        driver.setEmail("email@com");
        driver.setPassword("QWQWqwqw!1");
        driver.setRole(Role.DRIVER);
        when(repository.findByEmail(driver.getEmail())).thenReturn(Optional.empty());
        userService.save(driver);
        BigDecimal balance = ((Driver) driver).getBalance();
        assertEquals(balance, BigDecimal.ZERO);
    }

    @Test
    void saveUserVerifySaveInvoked() {
        user.setRole(Role.PASSENGER);
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        userService.save(user);
        verify(repository, times(1)).save(user);
    }
}