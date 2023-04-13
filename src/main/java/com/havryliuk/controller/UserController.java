package com.havryliuk.controller;

import com.havryliuk.model.User;
import com.havryliuk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }


    @GetMapping("/info")//todo rename to userPage or so
    public ModelAndView userInfo(ModelAndView modelAndView) {
        log.trace("user details page");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", user);//todo try to get user from thymeleaf
        modelAndView.addObject("activePage", "myAccount");
        modelAndView.addObject("subPage", "info");
        modelAndView.setViewName("users/info");
        return modelAndView;
    }


    @GetMapping("/{id}")
    public User getById(@PathVariable String id) {
        return service.getById(id).orElseThrow(() -> new IllegalArgumentException("User is not found"));
    }



}
