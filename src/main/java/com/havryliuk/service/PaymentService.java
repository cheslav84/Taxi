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
        BigDecimal balance = getUserBalance(user);
        balance = balance.add(rechargeValue);
        setUserBalance(user, balance);
        userRepository.save(user);
    }


    public void withdraw(User user, BigDecimal withdrawValue) throws PaymentException {
        BigDecimal balance = getUserBalance(user);
        checkIfWithdrawValueNotExceedBalance(balance, withdrawValue);
        balance = balance.subtract(withdrawValue);
        setUserBalance(user, balance);
        userRepository.save(user);
    }


    public void saveCompanyBalance(CompanyBalance companyBalance) {
        companyBalanceRepository.save(companyBalance);
    }



    public void setPaymentPrices(User passenger, Trip trip, CompanyBalance companyBalanceEntity) throws PaymentException {
        BigDecimal tripPrice = trip.getPrice();
        BigDecimal passengerBalance = getUserBalance(passenger);
        User driver = trip.getDriver();

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


    private void setNewBalanceIfNotManager(User user, BigDecimal rechargeValue) {
        switch (user.getRole()) {
            case PASSENGER -> {
                BigDecimal balance = ((Passenger) user).getBalance();
                balance = balance.add(rechargeValue);
                ((Passenger) user).setBalance(balance);
            }
            case DRIVER -> {
                BigDecimal balance = ((Driver) user).getBalance();
                balance = balance.remainder(rechargeValue);
                ((Driver) user).setBalance(balance);
            }
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


    public Iterable<Tariffs> findAll () {
        return tariffsRepository.findAll();
    }

    public Tariffs getTariffByCarClass(CarClass carClass) {
        return tariffsRepository.getTariffByCarClass(carClass);
    }






    BigDecimal getDriverPart(BigDecimal tripPrice, Tariffs tariffs) {
        BigDecimal driverPartInPercent = BigDecimal.valueOf(tariffs.getDriverPartInPercent());
        return tripPrice
                .multiply(driverPartInPercent)
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
    }

//
//    BigDecimal getDriverPart(Trip trip) {
////    BigDecimal getDriverPart(TripDtoForDriverPage trip) {
//
//
//        Tariffs tariffs = getTariffByCarClass(trip.getCarClass());
//
//        BigDecimal driverPartInPercent = BigDecimal.valueOf(tariffs.getDriverPartInPercent());
//        BigDecimal price = trip.getPrice();
//        return price
//                .multiply(driverPartInPercent)
//                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
//    }




    //    private void setInitialBalanceIfNotManager(User user) {
//        switch (user.getRole()) {
//            case PASSENGER -> ((Passenger) user).setBalance(BigDecimal.ZERO);
//            case DRIVER -> ((Driver) user).setBalance(BigDecimal.ZERO);
//        }
//    }


}
