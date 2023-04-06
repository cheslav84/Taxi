package com.havryliuk.controller;

import com.havryliuk.dto.NewBalanceDto;
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
@RequestMapping("/users/balance")
public class BalanceController {

    private final UserService service;

    @Autowired
    public BalanceController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping
    public ModelAndView userBalance(ModelAndView modelAndView) {
        log.trace("user balance page");
        modelAndView.addObject("newBalanceDTO", new NewBalanceDto());
        setResponseProperties(modelAndView);
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PutMapping("/recharge")
    public ModelAndView rechargeBalance(@Valid NewBalanceDto newBalance, Errors errors, ModelAndView modelAndView) {
        log.trace("recharging user balance");
        if (errors.hasErrors()) {
            setResponseProperties(modelAndView);
            return modelAndView;
        }
        System.err.println(newBalance.getRechargeValue());
//        System.err.println(addValue);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.updateBalance(user, newBalance.getRechargeValue());
        System.err.println(user.getId());

        modelAndView.setViewName("redirect:/users/balance");

        return modelAndView;
    }


    private void setResponseProperties(ModelAndView modelAndView) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "myAccount");
        modelAndView.addObject("subPage", "balance");
        modelAndView.setViewName("users/balance");
    }

}
