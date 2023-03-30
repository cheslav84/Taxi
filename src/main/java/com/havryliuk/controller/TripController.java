package com.havryliuk.controller;

import com.havryliuk.persistence.model.*;

import com.havryliuk.service.TripService;
import com.havryliuk.util.google.map.GoogleAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/taxi")
public class TripController {
    private final GoogleAPI googleAPI;

    private final TripService tripService;
    @Autowired
    public TripController(GoogleAPI googleAPI, TripService tripService) {
        this.googleAPI = googleAPI;
        this.tripService = tripService;
    }

    @GetMapping
    public ModelAndView taxiPage(ModelAndView modelAndView) {
        log.trace("taxiPage");
        modelAndView.addObject("trip", new Trip());
        modelAndView.setViewName("taxi");
        return modelAndView;
    }

    @PostMapping
    public String create(@Valid Trip trip, Errors errors) {
        if (errors.hasErrors()) {
            return "/taxi";
        }
        googleAPI.setAddressLocation(trip.getOriginAddress());
        googleAPI.setAddressLocation(trip.getDestinationAddress());
        googleAPI.setDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAYED);
//        trip.setPassenger();//todo
        System.err.println(trip);

        tripService.save(trip);
        System.err.println(trip);
        return "redirect:/taxi";//todo
    }

    @ModelAttribute(name = "trip")
    public Trip trip() {
        return new Trip();
    }


    @ModelAttribute(name = "carClasses")
    public List<CarClass> getCarClasses() {
        return List.of(CarClass.values());
    }





}
