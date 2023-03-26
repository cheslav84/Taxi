package com.havryliuk.controller;

import com.havryliuk.model.Trip;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Slf4j
@Controller
@RequestMapping("/taxi")
public class TripController {


    @GetMapping
    public ModelAndView taxiPage(ModelAndView modelAndView) {
        log.info("taxiPage");
        modelAndView.setViewName("taxi");
        return modelAndView;
    }


    @PostMapping
//    public String create(Trip trip) {
    public String create(@ModelAttribute("trip") Trip trip) {
        System.err.println(trip);
        return "redirect:/taxi";
    }


    @ModelAttribute(name = "trip")
    public Trip departureAddress() {
        return new Trip();
    }

//    @ModelAttribute(name = "arrivalAddress")
//    public Address arrivalAddress() {
//        return new Address();
//    }


//    @ResponseBody
//    @PostMapping
//    public String create(@RequestBody Address departureAddress) {
//        log.info("departureAddress {}", departureAddress);
//        return "trip";
//    }
}
