package com.havryliuk.controller;

import com.havryliuk.dto.UserDTO;

import com.havryliuk.exceptions.UserAlreadyExistException;
import com.havryliuk.model.*;
import com.havryliuk.service.UserService;
import com.havryliuk.util.factories.UserFactory;
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
    private final UserFactory factory;
    private final UserService userService;

    @Autowired
    public AuthenticationController(UserFactory factory, UserService userService) {
        this.factory = factory;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ModelAndView registrationPage(ModelAndView modelAndView) {
        log.trace("registrationPage");
        modelAndView.addObject("userDTO", new UserDTO());
        addObjectsToModelOnNewUser(modelAndView);
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView createNewUser(@Valid UserDTO userDTO, Errors errors, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        log.trace("createNewUser");
        if (errors.hasErrors()) {
            log.debug("validation error");
            addObjectsToModelOnNewUser(modelAndView);
            return modelAndView;
        }
        try {
            User user = factory.createUser(userDTO);
            userService.save(user);
        } catch (UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            String warningMessage = "An account for '"+ userDTO.getEmail() + "' already exists.";
            redirectAttributes.addFlashAttribute("warningMessage", warningMessage);
            log.warn(warningMessage);
            modelAndView.setViewName("redirect:/auth/registration");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/auth/login");
        return modelAndView;
    }

    private void addObjectsToModelOnNewUser(ModelAndView modelAndView) {
        modelAndView.addObject("roles", List.of(Role.values()));
        modelAndView.addObject("activePage", "auth");
        modelAndView.addObject("subPage", "registration");
        modelAndView.setViewName("registration");
    }


    @GetMapping(("/login"))
    public ModelAndView loginPage(ModelAndView modelAndView) {
        log.trace("loginPage");
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
