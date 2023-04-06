package com.havryliuk.controller;

import com.havryliuk.dto.trips.TripDtoForPassenger;
import com.havryliuk.model.CarClass;
import com.havryliuk.model.Trip;
import com.havryliuk.model.*;

import com.havryliuk.service.TripService;
import com.havryliuk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TripService tripService;
    private final UserService userService;

    @Autowired
    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public ModelAndView newTripPage(ModelAndView modelAndView) {
        log.trace("newTrip page");
        modelAndView.addObject("trip", new Trip());
        setResponseProperties(modelAndView);
        return modelAndView;
    }


    @PostMapping("/new")
    public ModelAndView createTrip(@Valid Trip trip, Errors errors, ModelAndView modelAndView) {
        log.trace("createTrip");
        if (errors.hasErrors()) {
            setResponseProperties(modelAndView);
            return modelAndView;
        }
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tripService.save(trip, user);
        modelAndView.setViewName("redirect:/trips/users/all");
        return modelAndView;
    }

    private void setResponseProperties(ModelAndView modelAndView) {
        modelAndView.addObject("carClasses", List.of(CarClass.values()));
        modelAndView.addObject("activePage", "newTrip");
        modelAndView.setViewName("new-trip");
    }


    @GetMapping("/users/all")
    public ModelAndView allUserTrips(ModelAndView modelAndView) {
        log.trace("allUserTrips page");
        System.err.println("allUserTrips page");

//        final User user = userService.loadUserByUsername(principal.getName());

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TripDtoForPassenger> trips = tripService.getByUser(user);
        System.err.println(trips);
//        System.err.println(trips.get(0).getPassenger());
//        System.err.println(trips.get(0).getPassengerName());

        modelAndView.addObject("trips", trips);

        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "home");
        modelAndView.addObject("subPage", "allTrips");

        modelAndView.setViewName("trips/all-trips");
        return modelAndView;
    }



}
