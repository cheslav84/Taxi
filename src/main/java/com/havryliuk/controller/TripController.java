package com.havryliuk.controller;

import com.google.maps.model.LatLng;
import com.havryliuk.model.Address;
import com.havryliuk.model.Trip;
import com.havryliuk.util.google.Geocoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Slf4j
@Controller
@RequestMapping("/taxi")
public class TripController {

    private final Geocoder geocoder;
//    private final GeoApiContext context;

//    public TripController(@Autowired Geocoder geocoder, @Autowired GeoApiContext context) {
//        this.geocoder = geocoder;
//        this.context = context;
//    }

    public TripController(@Autowired Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    @GetMapping
    public ModelAndView taxiPage(ModelAndView modelAndView) {
        log.info("taxiPage");
        modelAndView.setViewName("taxi");
        return modelAndView;
    }


    @PostMapping
    public String create(@Valid Trip trip, Errors errors) {
            setAddressLocation(trip.getOriginAddress());
            setAddressLocation(trip.getDestinationAddress());
            System.err.println(trip);

        if (errors.hasErrors()) {
            return "/taxi";
        }
        return "redirect:/taxi";//todo


    }

    private void setAddressLocation(Address address) {
        LatLng location = geocoder.getLocation(address.getAddress());
        address.setLocation(location);
    }


    @ModelAttribute(name = "trip")
    public Trip departureAddress() {
        return new Trip();
    }

//    @ModelAttribute(name = "arrivalAddress")
//    public Address arrivalAddress() {
//        return new Address();
//    }




}
