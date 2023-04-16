package com.havryliuk.service;

import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.repository.CompanyBalanceRepository;
import com.havryliuk.repository.TariffsRepository;
import com.havryliuk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PaymentServiceTest {

    @Mock
    private TariffsRepository tariffsRepository;

    @Mock
    private CompanyBalanceRepository companyBalanceRepository;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private final TestingInstances testingInstances = new TestingInstances();


    @Test
    void assertThatBalanceRecharge() {
        user = testingInstances.getTestingPassenger();
        BigDecimal rechargeValue = new BigDecimal(100);
        BigDecimal expected = ((Passenger)user).getBalance().add(rechargeValue);
        paymentService.recharge(user, rechargeValue);
        BigDecimal actual = ((Passenger)user).getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void rechargeBalanceAssertSaving() {
        user = testingInstances.getTestingPassenger();
        paymentService.recharge(user, new BigDecimal(100));
        verify(repository, times(1)).save(user);
    }


    @Test
    void assertThatBalanceWithdrew() throws PaymentException {
        user = testingInstances.getTestingDriver();
        BigDecimal withdrawValue = new BigDecimal(100);
        BigDecimal expected = ((Driver)user).getBalance().subtract(withdrawValue);
        paymentService.withdraw(user, withdrawValue);
        BigDecimal actual = ((Driver)user).getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void withdrawBalanceAssertSaving() throws PaymentException {
        user = testingInstances.getTestingPassenger();
        paymentService.withdraw(user, new BigDecimal(100));
        verify(repository, times(1)).save(user);
    }

    @Test
    void withdrawSumExceedBalance() {
        user = testingInstances.getTestingDriver();
        BigDecimal balance = ((Driver)user).getBalance();
        BigDecimal withdrawSum = balance.add(new BigDecimal(1));
        Exception exception = assertThrows(PaymentException.class, () -> paymentService.withdraw(user, withdrawSum));
        assertEquals("Operation been canceled. The there are not enough funds in the account.",
                exception.getMessage());
    }


    @Test
    void saveCompanyBalance() {
        CompanyBalance companyBalance = testingInstances.getTestingCompanyBalance();
        paymentService.saveCompanyBalance(companyBalance);
        verify(companyBalanceRepository, times(1)).save(companyBalance);
    }


    @Test
    void setPaymentPrices() throws PaymentException {
        Trip trip = testingInstances.getTestingTrip();
        Passenger passenger = testingInstances.getTestingPassenger();
        Driver driver = (Driver) trip.getDriver();
        CompanyBalance companyBalanceEntity = testingInstances.getTestingCompanyBalance();
        Tariffs tariffs = testingInstances.getTestingStandardTariff();

        when(tariffsRepository.getTariffByCarClass(trip.getCarClass())).thenReturn(tariffs);

        BigDecimal tripPrice = trip.getPrice();
        passenger.setBalance(tripPrice.add(new BigDecimal(1)));

        BigDecimal passengerBalanceBefore = passenger.getBalance();
        BigDecimal driverBalanceBefore = driver.getBalance();
        BigDecimal companyBalanceBefore = companyBalanceEntity.getBalance();

        paymentService.setPaymentPrices(passenger, trip, companyBalanceEntity);

        BigDecimal passengerBalanceAfter = passenger.getBalance();
        BigDecimal driverBalanceAfter = driver.getBalance();
        BigDecimal companyBalanceAfter = companyBalanceEntity.getBalance();
        BigDecimal driverPart = paymentService.getDriverPart(tripPrice, tariffs);

        assertEquals(passengerBalanceBefore.subtract(tripPrice), passengerBalanceAfter);
        assertEquals(driverBalanceBefore.add(driverPart), driverBalanceAfter);
        assertEquals(companyBalanceBefore.add(tripPrice).subtract(driverPart), companyBalanceAfter);
    }

    @Test
    void setPaymentPricesWhenTripPaid() {
        Trip trip = testingInstances.getTestingTrip();
        trip.setPaymentStatus(PaymentStatus.PAID);
        Passenger passenger = testingInstances.getTestingPassenger();
        CompanyBalance companyBalanceEntity = testingInstances.getTestingCompanyBalance();
        Exception exception = assertThrows(
                PaymentException.class, () -> paymentService.setPaymentPrices(passenger, trip, companyBalanceEntity)
        );
        assertEquals("Trip has already paid.", exception.getMessage());
    }

    @Test
    void setPaymentPricesWhenDriverIsNotAssigned() {
        Trip trip = testingInstances.getTestingTrip();
        trip.setDriver(null);
        Passenger passenger = testingInstances.getTestingPassenger();
        CompanyBalance companyBalanceEntity = testingInstances.getTestingCompanyBalance();
        Exception exception = assertThrows(
                PaymentException.class, () -> paymentService.setPaymentPrices(passenger, trip, companyBalanceEntity)
        );
        assertEquals("You cannot made payment yet. The driver is not assigned.", exception.getMessage());
    }

    @Test
    void setPaymentPricesWhenPassengerDoNotHasEnough() {
        Trip trip = testingInstances.getTestingTrip();
        Passenger passenger = testingInstances.getTestingPassenger();
        CompanyBalance companyBalanceEntity = testingInstances.getTestingCompanyBalance();
        BigDecimal tripPrice = trip.getPrice();
        passenger.setBalance(tripPrice.subtract(new BigDecimal(1)));
        Exception exception = assertThrows(
                PaymentException.class, () -> paymentService.setPaymentPrices(passenger, trip, companyBalanceEntity)
        );
        assertEquals("Operation been canceled. The there are not enough funds in the account.",
                exception.getMessage());
    }



    @Test
    void getCompanyBalance() {
        CompanyBalance companyBalance = testingInstances.getTestingCompanyBalance();
        ArrayList<CompanyBalance> balances = new ArrayList<>();
        balances.add(companyBalance);
        when(companyBalanceRepository.findAll()).thenReturn(balances);
        CompanyBalance actual = paymentService.getCompanyBalance();
        assertEquals(companyBalance, actual);
    }


    @Test
    void findAllTariffs() {
        Iterable<Tariffs> tariffs = testingInstances.getTestingTariffs();
        when(tariffsRepository.findAll()).thenReturn(tariffs);
        Iterable<Tariffs> actual = tariffsRepository.findAll();
        assertEquals(tariffs, actual);
    }


    @ParameterizedTest
    @MethodSource("correctTariff")
    void getTariffByCarClass(CarClass carClass, Tariffs tariffs) {
        when(tariffsRepository.getTariffByCarClass(carClass)).thenReturn(tariffs);
        Tariffs actual = paymentService.getTariffByCarClass(carClass);
        assertEquals(actual, tariffs);

    }

    private static Stream<Arguments> correctTariff () {
        return Stream.of(
                Arguments.of(CarClass.STANDARD,  new TestingInstances().getTestingStandardTariff()),
                Arguments.of(CarClass.BUSINESS,  new TestingInstances().getTestingBusinessTariff()),
                Arguments.of(CarClass.VIP,  new TestingInstances().getTestingVipTariff()),
                Arguments.of(CarClass.BUS_MINIVAN,  new TestingInstances().getTestingBusTariff())
        );
    }


}