package com.havryliuk.service;

import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.repository.CompanyBalanceRepository;
import com.havryliuk.repository.TariffsRepository;
import com.havryliuk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class PaymentService {

    private final TariffsRepository tariffsRepository;
    private final CompanyBalanceRepository companyBalanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(TariffsRepository tariffsRepository, UserRepository userRepository,
                          CompanyBalanceRepository companyBalanceRepository) {
        this.tariffsRepository = tariffsRepository;
        this.userRepository = userRepository;
        this.companyBalanceRepository = companyBalanceRepository;
    }

    public void recharge(User user, BigDecimal rechargeValue) {
        log.trace("recharging '{}' balance", user.getEmail());
        BigDecimal balance = getUserBalance(user);
        balance = balance.add(rechargeValue);
        setUserBalance(user, balance);
        userRepository.save(user);
        log.debug("User '{}' balance has been recharged with {}", user.getEmail(), rechargeValue);
    }


    public void withdraw(User user, BigDecimal withdrawValue) throws PaymentException {
        log.trace("withdrawing '{}' balance", user.getEmail());
        BigDecimal balance = getUserBalance(user);
        checkIfWithdrawValueNotExceedBalance(balance, withdrawValue);
        balance = balance.subtract(withdrawValue);
        setUserBalance(user, balance);
        userRepository.save(user);
        log.debug("User '{}' withdraw the {} amount.", user.getEmail(), withdrawValue);

    }

    public void saveCompanyBalance(CompanyBalance companyBalance) {
        companyBalanceRepository.save(companyBalance);
        log.debug("Company balance was saved. New balance: {}.", companyBalance.getBalance());
    }


    public void setPaymentPrices(User passenger, Trip trip, CompanyBalance companyBalanceEntity)
            throws PaymentException {
        log.trace("setting payment prices");

        BigDecimal tripPrice = trip.getPrice();
        BigDecimal passengerBalance = getUserBalance(passenger);
        User driver = trip.getDriver();

        checkIfNotPaid(trip);
        checkIfDriverAssigned(driver);
        checkIfWithdrawValueNotExceedBalance(passengerBalance, tripPrice);

        Tariffs tariffs = getTariffByCarClass(trip.getCarClass());

        BigDecimal driverBalance = getUserBalance(driver);
        BigDecimal companyBalance = companyBalanceEntity.getBalance();
        BigDecimal driverPart = getDriverPart(tripPrice, tariffs);
        BigDecimal companyPart = tripPrice.subtract(driverPart);

        passengerBalance = passengerBalance.subtract(tripPrice);
        driverBalance = driverBalance.add(driverPart);
        companyBalance = companyBalance.add(companyPart);

        setUserBalance(passenger, passengerBalance);
        setUserBalance(driver, driverBalance);
        companyBalanceEntity.setBalance(companyBalance);
    }


    public CompanyBalance getCompanyBalance() {
        return companyBalanceRepository
                .findAll()
                .iterator()
                .next();
    }

    public Iterable<Tariffs> findAll () {
        return tariffsRepository.findAll();
    }

    public Tariffs getTariffByCarClass(CarClass carClass) {
        return tariffsRepository.getTariffByCarClass(carClass);
    }


    public BigDecimal getDriverPart(BigDecimal tripPrice, Tariffs tariffs) {
        BigDecimal driverPartInPercent = BigDecimal.valueOf(tariffs.getDriverPartInPercent());
        return tripPrice
                .multiply(driverPartInPercent)
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
    }

    private void checkIfNotPaid(Trip trip) throws PaymentException {
        if(trip.getPaymentStatus().equals(PaymentStatus.PAID)) {
            String message = "Trip has already paid.";
            log.warn(message);
            throw new PaymentException(message);
        }
    }


    private void checkIfDriverAssigned(User driver) throws PaymentException {
        if(driver == null) {
            String message = "You cannot made payment yet. The driver is not assigned.";
            log.warn(message);
            throw new PaymentException(message);
        }
    }

    private void checkIfWithdrawValueNotExceedBalance(BigDecimal balance, BigDecimal withdrawValue)
            throws PaymentException {
        if (withdrawValue.compareTo(balance) > 0) {
            String message = "Operation been canceled. The there are not enough funds in the account.";
            log.warn(message);
            throw new PaymentException(message);
        }
    }


    private BigDecimal getUserBalance(User user) {
        switch (user.getRole()) {
            case PASSENGER -> {
                return  ((Passenger) user).getBalance();
            }
            case DRIVER -> {
                return  ((Driver) user).getBalance();
            }
            default -> throw new IllegalArgumentException("That user has not his money account in application.");
        }
    }


    private void setUserBalance(User user, BigDecimal balance) {
        switch (user.getRole()) {
            case PASSENGER -> ((Passenger) user).setBalance(balance);
            case DRIVER -> ((Driver) user).setBalance(balance);
        }
    }




}
