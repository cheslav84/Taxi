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

    @Autowired
    public AuthenticationController(UserFromDtoMapper mapper, UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ModelAndView registrationPage(ModelAndView modelAndView) {
        log.trace("registrationPage");
        modelAndView.addObject("userDto", new UserDto());
        setModelAttributes(modelAndView);
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView createNewUser(@Valid UserDto userDto, Errors errors,
                                      ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        log.trace("createNewUser");
        if (errors.hasErrors()) {
            log.debug("validation error");
            modelAndView.addObject("userDto", userDto);
            setModelAttributes(modelAndView);
            return modelAndView;
        }
        try {
            User user = mapper.map(userDto);
            userService.save(user);
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
        return modelAndView;
    }

    private void setModelAttributes(ModelAndView modelAndView) {
        modelAndView.addObject("roles", List.of(Role.values()));
        modelAndView.addObject("activePage", "auth");
        modelAndView.addObject("subPage", "registration");
        modelAndView.setViewName("registration");
    }

    @GetMapping(("/login"))
    public ModelAndView loginPage(ModelAndView modelAndView) {
        log.trace("loginPage");
        modelAndView.addObject("activePage", "auth");
        modelAndView.addObject("subPage", "login");
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(("/logout"))
    public ModelAndView logoutPage(ModelAndView modelAndView) {
        log.trace("logoutPage");
        modelAndView.setViewName("logout");
        return modelAndView;
    }

}
