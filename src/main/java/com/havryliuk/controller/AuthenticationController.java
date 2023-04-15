package com.havryliuk.controller;

import com.havryliuk.dto.UserDto;

import com.havryliuk.exceptions.UserAlreadyExistException;
import com.havryliuk.model.*;
import com.havryliuk.service.UserService;
import com.havryliuk.util.mappers.UserFromDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserFromDtoMapper mapper;
    private final UserService userService;

    @SuppressWarnings("unused")
    @Autowired
    public AuthenticationController(UserFromDtoMapper mapper, UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ModelAndView registrationPage(ModelAndView modelAndView) {
        log.trace("get:/auth/registration");
        modelAndView.addObject("userDto", new UserDto());
        setModelAttributes(modelAndView);
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView createNewUser(@Valid UserDto userDto, Errors errors,
                                      ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        log.trace("post:/auth/registration");
        if (errors.hasErrors()) {
            modelAndView.addObject("userDto", userDto);
            setModelAttributes(modelAndView);
            log.info("registration failed. Cause: {}", errors.getAllErrors());
            return modelAndView;
        }
        User user;
        try {
            user = mapper.map(userDto);
            user = userService.save(user);
        } catch (UserAlreadyExistException e) {
            String warningMessage = "An account for '"+ userDto.getEmail() + "' already exists.";
            modelAndView.addObject("warningMessage", warningMessage);
            modelAndView.addObject("userDto", userDto);
            modelAndView.setViewName("registration");
            setModelAttributes(modelAndView);
            log.warn(warningMessage);
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/auth/login");
        log.info("new {} with id={} was created.", user.getRole(), user.getId());
        return modelAndView;
    }


    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        log.trace("get:/auth/login");
        modelAndView.addObject("activePage", "auth");
        modelAndView.addObject("subPage", "login");
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logoutPage(ModelAndView modelAndView) {
        log.trace("get:/auth/logout");
        modelAndView.setViewName("logout");
        return modelAndView;
    }

    private void setModelAttributes(ModelAndView modelAndView) {
        modelAndView.addObject("roles", List.of(Role.values()));
        modelAndView.addObject("activePage", "auth");
        modelAndView.addObject("subPage", "registration");
        modelAndView.setViewName("registration");
    }


}
