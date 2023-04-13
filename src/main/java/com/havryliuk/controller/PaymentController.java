package com.havryliuk.controller;

import com.havryliuk.dto.NewBalanceDto;
import com.havryliuk.dto.trips.TripDtoForPassengerPage;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.service.PaymentService;
import com.havryliuk.service.tripServices.TripService;
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
    private final TripService tripService;

    @Autowired
    public PaymentController(PaymentService paymentService, TripService tripService) {
        this.paymentService = paymentService;
        this.tripService = tripService;
    }



//    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/balance")
    public ModelAndView userBalance(ModelAndView modelAndView) {
        log.trace("user balance page");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setModelAttributes(modelAndView, user);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @PutMapping("/trips/pay/{tripId}")
    public ModelAndView payForTrip(@PathVariable String tripId, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            tripService.payForTrip(user, tripId);
        } catch (PaymentException e) {
            TripDtoForPassengerPage trip = tripService.getDtoFoPassengerById(tripId);// todo think if would be better receive that DTO from front-end
            modelAndView.addObject("errorMessage", e.getMessage());// todo code repeats in another controller think of refactoring
            modelAndView.addObject("trip", trip);
            modelAndView.setViewName("trips/trip-details");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/passengers/manage/" + tripId);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @PutMapping("/balance/recharge")
    public ModelAndView rechargeBalance(@Valid NewBalanceDto value, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (errors.hasErrors()) {
            setModelAttributes(modelAndView, user);
            return modelAndView;
        }
        paymentService.recharge(user, value.getValue());
        modelAndView.setViewName("redirect:/balance");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping("/balance/withdraw")
    public ModelAndView withdrawFunds(@Valid NewBalanceDto newBalance, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (errors.hasErrors()) {
            setModelAttributes(modelAndView, user);
            return modelAndView;
        }
        try {
            paymentService.withdraw(user, newBalance.getValue());
        } catch (PaymentException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            setModelAttributes(modelAndView, user);
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/balance");
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
