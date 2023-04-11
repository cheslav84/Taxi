package com.havryliuk.controller;

import com.havryliuk.dto.NewBalanceDto;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/balance")
public class PaymentController {

    private final UserService service;

    @Autowired
    public PaymentController(UserService service) {
        this.service = service;
    }


//    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping
    public ModelAndView userBalance(ModelAndView modelAndView) {
        log.trace("user balance page");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setModelAttributes(modelAndView, user);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @PutMapping("/recharge")
    public ModelAndView rechargeBalance(@Valid NewBalanceDto value, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (errors.hasErrors()) {
            setModelAttributes(modelAndView, user);
            return modelAndView;
        }
        service.recharge(user, value.getValue());
        modelAndView.setViewName("redirect:/balance");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping("/withdraw")
    public ModelAndView withdrawFunds(@Valid NewBalanceDto newBalance, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (errors.hasErrors()) {
            setModelAttributes(modelAndView, user);
            return modelAndView;
        }
        try {
            service.withdraw(user, newBalance.getValue());
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
