package com.havryliuk.controller;

import com.havryliuk.dto.PageWrapper;
import com.havryliuk.dto.trips.TripDtoForDriver;
import com.havryliuk.dto.trips.TripDtoForPassenger;
import com.havryliuk.dto.trips.TripDtoForUser;
import com.havryliuk.model.CarClass;
import com.havryliuk.model.Trip;
import com.havryliuk.model.*;

import com.havryliuk.service.TripService;
import com.havryliuk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static int INITIAL_PAGE_NUMBER = 1;//todo set size in user page and cookies
    private static int NUMBER_OF_ITEMS_PER_PAGE = 4;

    private final TripService tripService;
    private final UserService userService;

    //todoвказати параметри пагінації

    @Autowired
    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public ModelAndView newTripPage(ModelAndView modelAndView) {
        log.trace("newTrip page");
        modelAndView.addObject("trip", new Trip());
        setModelAttributesForCreatePage(modelAndView);
        return modelAndView;
    }


    @PostMapping("/new")
    public ModelAndView createTrip(@Valid Trip trip, Errors errors, ModelAndView modelAndView) {
        log.trace("createTrip");
        if (errors.hasErrors()) {
            setModelAttributesForCreatePage(modelAndView);
            return modelAndView;
        }
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tripService.save(trip, user);
        modelAndView.setViewName("redirect:/trips/users/all");
        return modelAndView;
    }

    private void setModelAttributesForCreatePage(ModelAndView modelAndView) {
        modelAndView.addObject("carClasses", List.of(CarClass.values()));
        modelAndView.addObject("activePage", "newTrip");
        modelAndView.setViewName("new-trip");
    }



    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/users/all")
    public ModelAndView allUserTrips(ModelAndView modelAndView) {
        return allUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/users/all/{currentPageNo}/{size}")
    public ModelAndView allUserTripsPaginated(
                                 @PathVariable int currentPageNo,
                                 @PathVariable int size,
                                 ModelAndView modelAndView) {
        log.trace("/users/all/{currentPageNo}/{size}");
        String requestURI = "/trips/users/all";//todo think of getting from request (then will need to get rid of PathVariables)
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//todo think to reuse of code
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForPassenger> tripsPage = tripService.getAllByUser(user, pageable);
        PageWrapper<TripDtoForPassenger> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForTripPages(modelAndView, user, page);
        modelAndView.addObject("subPage", "All trips");
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/users/active")
    public ModelAndView activeUserTrips(ModelAndView modelAndView) {
        return activeUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/users/active/{currentPageNo}/{size}")
    public ModelAndView activeUserTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/users/active/{currentPageNo}/{size}");
        String requestURI = "/trips/users/active";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForPassenger> tripsPage = tripService.getActiveByUser(user, pageable);
        PageWrapper<TripDtoForPassenger> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForTripPages(modelAndView, user, page);
        modelAndView.addObject("subPage", "Active trips");
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/users/past")
    public ModelAndView pastUserTrips(ModelAndView modelAndView) {
        return pastUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/users/past/{currentPageNo}/{size}")
    public ModelAndView pastUserTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/users/past/{currentPageNo}/{size}");
        String requestURI = "/trips/users/past";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForPassenger> tripsPage = tripService.getPastByUser(user, pageable);
        PageWrapper<TripDtoForPassenger> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForTripPages(modelAndView, user, page);
        modelAndView.addObject("subPage", "Past trips");
        return modelAndView;
    }

    private void setModelAttributesForTripPages(
            ModelAndView modelAndView, User user, PageWrapper<? extends TripDtoForUser> page) {
        modelAndView.addObject("page", page);
        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "myAccount");
        modelAndView.setViewName("trips/passenger-trips");;

    }



    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/drivers/active")
    public ModelAndView allActiveTrips(ModelAndView modelAndView) {
        return allActiveTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/drivers/active/{currentPageNo}/{size}")
    public ModelAndView allActiveTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/drivers/active/{currentPageNo}/{size}");
        String requestURI = "/trips/drivers/active";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForDriver> tripsPage = tripService.getAllNew(pageable);
        PageWrapper<TripDtoForDriver> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);

        System.err.println(tripsPage.getContent());
        setModelAttributesForTripPages(modelAndView, user, page);
        modelAndView.addObject("subPage", "Past trips");
        return modelAndView;
    }




}
