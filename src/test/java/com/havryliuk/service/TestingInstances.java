package com.havryliuk.service;

import com.havryliuk.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestingInstances {

    public Passenger getTestingPassenger() {
        Passenger passenger =  new Passenger();
        passenger.setId("id");
        passenger.setEmail("email@com");
        passenger.setPassword("QWQWqwqw!1");
        passenger.setName("userName");
        passenger.setRole(Role.PASSENGER);
        passenger.setBalance(new BigDecimal(100));
        return passenger;
    }

    public Driver getTestingDriver() {
        Driver driver =  new Driver();
        driver.setId("id");
        driver.setEmail("email@com");
        driver.setPassword("QWQWqwqw!1");
        driver.setName("userName");
        driver.setRole(Role.DRIVER);
        driver.setBalance(new BigDecimal(100));
        return driver;
    }

    public CompanyBalance getTestingCompanyBalance() {
        CompanyBalance companyBalance = new CompanyBalance();
        companyBalance.setBalance(new BigDecimal(100));
        return companyBalance;
    }


    public Trip getTestingTrip() {
        Trip trip = new Trip();
        trip.setDriver(this.getTestingDriver());
        trip.setCarClass(CarClass.STANDARD);
        trip.setPrice(new BigDecimal(100));
        trip.setPaymentStatus(PaymentStatus.NOT_PAID);

        return trip;
    }

    public Tariffs getTestingStandardTariff() {
        Tariffs tariffs = getTestingTariff();
        tariffs.setCarClass(CarClass.STANDARD);
        return tariffs;
    }

    public Tariffs getTestingBusinessTariff() {
        Tariffs tariffs = getTestingTariff();
        tariffs.setCarClass(CarClass.BUSINESS);
        return tariffs;
    }

    public Tariffs getTestingVipTariff() {
        Tariffs tariffs = getTestingTariff();
        tariffs.setCarClass(CarClass.VIP);
        return tariffs;
    }

    public Tariffs getTestingBusTariff() {
        Tariffs tariffs = getTestingTariff();
        tariffs.setCarClass(CarClass.BUS_MINIVAN);
        return tariffs;
    }
    private Tariffs getTestingTariff() {
        Tariffs tariffs = new Tariffs();
        tariffs.setDriverPartInPercent(50);
        tariffs.setPricePerKilometer(new BigDecimal(10));
        return tariffs;
    }

    public List<Tariffs> getTestingTariffs() {
        List<Tariffs> tariffs = new ArrayList<>();
        tariffs.add(getTestingStandardTariff());
        tariffs.add(getTestingBusinessTariff());
        tariffs.add(getTestingVipTariff());
        tariffs.add(getTestingBusTariff());
        return tariffs;
    }




}
