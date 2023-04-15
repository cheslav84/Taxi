package com.havryliuk.controller;

import com.havryliuk.dto.NewBalanceDto;
import com.havryliuk.dto.trips.TripDtoForPassengerPage;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.service.PaymentService;
import com.havryliuk.service.tripService.PassengerTripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping
public class PaymentController {

    private final PaymentService paymentService;
    private final PassengerTripService passengerTripService;

    @Autowired
    public PaymentController(PaymentService paymentService, PassengerTripService passengerTripService) {
        this.paymentService = paymentService;
        this.passengerTripService = passengerTripService;
    }

    @GetMapping("/balance")
    public ModelAndView userBalance(ModelAndView modelAndView) {
        log.trace("get:/balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setModelAttributes(modelAndView, user);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @PutMapping("/trips/pay/{id}")
    public ModelAndView payForTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("put:/trips/pay/{}", id);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            passengerTripService.payForTrip(user, id);
        } catch (PaymentException e) {
            TripDtoForPassengerPage trip = passengerTripService.getDtoFoPassengerById(id);
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.addObject("trip", trip);
            modelAndView.setViewName("trips/trip-details");
            log.info("payment for trip {} wasn't successful. Cause: {}", id, e.getMessage());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/passengers/manage/" + id);
        log.info("payment for trip {} was successful.", id);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @PutMapping("/balance/recharge")
    public ModelAndView rechargeBalance(@Valid NewBalanceDto newBalanceVal, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (errors.hasErrors()) {
            setModelAttributes(modelAndView, user);
            log.info("balance data is invalid: {}", errors.getAllErrors());
            return modelAndView;
        }
        paymentService.recharge(user, newBalanceVal.getValue());
        modelAndView.setViewName("redirect:/balance");
        log.info("{} with id={} recharged with {} amount.", user.getRole(), user.getId(), newBalanceVal.getValue());
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping("/balance/withdraw")
    public ModelAndView withdrawFunds(@Valid NewBalanceDto newBalanceVal, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (errors.hasErrors()) {
            setModelAttributes(modelAndView, user);
            log.info("balance data is invalid: {}", errors.getAllErrors());
            return modelAndView;
        }
        try {
            paymentService.withdraw(user, newBalanceVal.getValue());
        } catch (PaymentException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            setModelAttributes(modelAndView, user);
            log.info("{} with id={} failed withdrew {} amount.",
                    user.getRole(), user.getId(), newBalanceVal.getValue());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/balance");
        log.info("{} with id={} withdrew {} amount.", user.getRole(), user.getId(), newBalanceVal.getValue());
        return modelAndView;
    }

    private void setModelAttributes(ModelAndView modelAndView, User user) {
        modelAndView.addObject("newBalanceDTO", new NewBalanceDto());
        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "myAccount");
        modelAndView.addObject("subPage", "balance");
        modelAndView.setViewName("users/balance");
    }

}
