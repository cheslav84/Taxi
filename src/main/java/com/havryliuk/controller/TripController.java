package com.havryliuk.controller;

import com.havryliuk.model.CarClass;
import com.havryliuk.model.PaymentStatus;
import com.havryliuk.model.Trip;
import com.havryliuk.model.TripStatus;
import com.havryliuk.model.*;

import com.havryliuk.service.TripService;
import com.havryliuk.util.google.map.GoogleService;
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
@RequestMapping("/trips")
public class TripController {
    private final GoogleService googleService;

    private final TripService tripService;
    @Autowired
    public TripController(GoogleService googleService, TripService tripService) {
        this.googleService = googleService;
        this.tripService = tripService;
    }

    @GetMapping("/new")
    public ModelAndView newTripPage(ModelAndView modelAndView) {
        log.trace("newTrip page");
        modelAndView.addObject("trip", new Trip());
        addObjectsToModelOnNewTrip(modelAndView);
        return modelAndView;
    }


    @PostMapping("/new")
    public ModelAndView createTrip(@Valid Trip trip, Errors errors, ModelAndView modelAndView) {
        if (errors.hasErrors()) {
            addObjectsToModelOnNewTrip(modelAndView);
            return modelAndView;
        }
        googleService.setAddressLocation(trip.getOriginAddress());
        googleService.setAddressLocation(trip.getDestinationAddress());
        googleService.setDistanceAndDuration(trip);
        trip.setTripStatus(TripStatus.NEW);
        trip.setPaymentStatus(PaymentStatus.NOT_PAYED);

//        trip.setPassenger();//todo
//        tripService.save(trip);
        System.err.println(trip);
        modelAndView.setViewName("redirect:/trips/new");
        return modelAndView;
    }

    private void addObjectsToModelOnNewTrip(ModelAndView modelAndView) {
        modelAndView.addObject("carClasses", List.of(CarClass.values()));
        modelAndView.addObject("activePage", "newTrip");
        modelAndView.setViewName("new-trip");
    }


    @GetMapping("/all")
    public ModelAndView allUserTrips(ModelAndView modelAndView) {
        log.trace("user home page");

//        User user = service.getById(id).orElseThrow(() -> new IllegalArgumentException("User is not found"));
//        System.err.println(user);
//        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "home");
        modelAndView.addObject("subPage", "allTrips");
        modelAndView.setViewName("user/trips/all-trips");
        return modelAndView;
    }









//    @PostMapping
//    public String create(@Valid Trip trip, Errors errors, ModelAndView modelAndView) {
//        if (errors.hasErrors()) {
//            modelAndView.addObject("activePage", "taxi");
//            return "/taxi";
//        }
//        googleService.setAddressLocation(trip.getOriginAddress());
//        googleService.setAddressLocation(trip.getDestinationAddress());
//        googleService.setDistanceAndDuration(trip);
//        trip.setTripStatus(TripStatus.NEW);
//        trip.setPaymentStatus(PaymentStatus.NOT_PAYED);
////        trip.setPassenger();//todo
//        System.err.println(trip);
//
//        tripService.save(trip);
//        System.err.println(trip);
//        return "redirect:/taxi";//todo
//    }

//    @ModelAttribute(name = "trip")
//    public Trip trip() {
//        return new Trip();
//    }

//    @ModelAttribute(name = "activePage")
//    public String activePage() {
//        return "taxi";
//    }







}
