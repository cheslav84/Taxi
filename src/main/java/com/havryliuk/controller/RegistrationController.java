package com.havryliuk.controller;

import com.havryliuk.dto.UserDTO;

import com.havryliuk.persistence.model.User;
import com.havryliuk.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationController {


//    @Autowired
//    private CaptchaService captchaService;

    private final RegistrationService registrationService;


    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ModelAndView registrationPage(ModelAndView modelAndView) {
        log.trace("registrationPage");
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping
    public String create(UserDTO userDTO, Errors errors) {
//    public String create(@Valid UserDTO userDTO, Errors errors) {


        System.err.println(userDTO);
        User user = registrationService.registerUser(userDTO);
        System.err.println(user);


        if (errors.hasErrors()) {
            return "/registration";
        }
        return "redirect:/registration";//todo
    }




    @ModelAttribute(name = "userDTO")
    public UserDTO getUser() {
        return new UserDTO();
    }





}
