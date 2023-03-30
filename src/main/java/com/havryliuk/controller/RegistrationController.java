package com.havryliuk.controller;

import com.havryliuk.dto.UserDTO;

import com.havryliuk.exceptions.UserAlreadyExistException;
import com.havryliuk.persistence.model.Role;
import com.havryliuk.persistence.model.User;
import com.havryliuk.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ModelAndView registrationPage(ModelAndView modelAndView) {
        log.trace("registrationPage");

//        List<Role> roles = List.of(Role.values());
//        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("registration");
//        modelAndView.addObject("warningMessage",
//                "An account for that username/email already exists.");
        return modelAndView;
    }

    @PostMapping
    public String create(@Valid UserDTO userDTO, Errors errors, RedirectAttributes redirectAttributes) {
        System.err.println(userDTO);
        if (errors.hasErrors()) {
            return "/registration";//todo
        }
        try {
            User user = registrationService.registerUser(userDTO);
            System.err.println(user);
        } catch (UserAlreadyExistException e) {
            redirectAttributes.addFlashAttribute("userDTO", userDTO);
            String warningMessage = "An account for '"+ userDTO.getEmail() + "' already exists.";
            redirectAttributes.addFlashAttribute("warningMessage", warningMessage);
            log.warn(warningMessage);
            return "redirect:/registration";
        }
        return "redirect:/registration";//todo
    }


    @ModelAttribute(name = "userDTO")
    public UserDTO getUser() {
        return new UserDTO();
    }

    @ModelAttribute(name = "roles")
    public List<Role> getRoles() {
        return List.of(Role.values());
    }



}
