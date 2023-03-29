package com.havryliuk.controller;

import com.havryliuk.persistence.model.PaymentStatus;
import com.havryliuk.persistence.model.Trip;

import com.havryliuk.persistence.model.TripStatus;
import com.havryliuk.service.TripService;
import com.havryliuk.util.google.map.GoogleAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


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
        modelAndView.setViewName("taxi");
        return modelAndView;
    }

    @PostMapping
    public String create(@Valid Trip trip, Errors errors) {
        googleAPI.setAddressLocation(trip.getOriginAddress());
        googleAPI.setAddressLocation(trip.getDestinationAddress());
        googleAPI.setDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAYED);
//        trip.setPassenger();//todo
        System.err.println(trip);

        tripService.save(trip);
        System.err.println(trip);
        if (errors.hasErrors()) {
            return "/taxi";
        }
        return "redirect:/taxi";//todo
    }

    @ModelAttribute(name = "trip")
    public Trip trip() {
        return new Trip();
    }







}
