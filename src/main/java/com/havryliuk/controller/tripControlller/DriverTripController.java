package com.havryliuk.controller.tripControlller;

import com.havryliuk.dto.PageWrapper;
import com.havryliuk.dto.SortWrapper;
import com.havryliuk.dto.trips.*;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;

import com.havryliuk.service.tripService.DriverTripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
@RequestMapping("/trips")
public class DriverTripController {

    private static final String INITIAL_PAGE_NUMBER = "1";
    private static final String NUMBER_OF_ITEMS_PER_PAGE = "4";
    private final DriverTripService driverTripService;


    @Autowired
    public DriverTripController(DriverTripService driverTripService) {
        this.driverTripService = driverTripService;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/details/{id}")
    public ModelAndView getTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("get:/trips/drivers/details/{}", id);
        TripDtoForDriverDetailed trip = driverTripService.getDtoById(id);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("trip", trip);
        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "Find passengers");
        modelAndView.setViewName("trips/take-passenger");
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/take/{id}")
    public ModelAndView setDriver(@PathVariable String id, TripDtoForDriverDetailed tripDto,
                                  ModelAndView modelAndView) {
        log.trace("put:/trips/drivers/take/{}", id);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trip trip = driverTripService.getById(id);
        driverTripService.saveDriverAndTaxiLocation(trip, user, tripDto.getTaxiLocationAddress());
        modelAndView.setViewName("redirect:/trips/drivers/active");
        log.trace("driver {} was set to trip {}", user.getEmail(), id);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/available")
    public ModelAndView allActiveTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,desc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace("get:{}",requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        CarClass carClass = ((Driver) user).getCar().getCarClass();
        Page<TripDtoShortInfo> tripsPage = driverTripService.getAllNew(carClass, pageable);
        PageWrapper<TripDtoShortInfo> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForGetPassengerPages(modelAndView, page, sort);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/all")
    public ModelAndView allDriverTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,desc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace("get:{}",requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        Page<TripDtoForDriverPage> tripsPage = driverTripService.getAllByDriver(user, pageable);
        PageWrapper<TripDtoForDriverPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        String subPage = "All trips";
        setModelAttributesForAccountTripPages(modelAndView, page, subPage, sort);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/active")
    public ModelAndView activeDriverTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,desc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace("get:{}",requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        Page<TripDtoForDriverPage> tripsPage = driverTripService.getActiveByDriver(user, pageable);
        PageWrapper<TripDtoForDriverPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        String subPage = "Active trips";
        setModelAttributesForAccountTripPages(modelAndView, page, subPage, sort);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/past")
    public ModelAndView pastDriverTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,desc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace("get:{}",requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        Page<TripDtoForDriverPage> tripsPage = driverTripService.getPastByDriver(user, pageable);
        PageWrapper<TripDtoForDriverPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        String subPage = "Past trips";
        setModelAttributesForAccountTripPages(modelAndView, page, subPage, sort);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/manage/{id}")
    public ModelAndView driversTripDetailsPage(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("get:/trips/drivers/manage/{}", id);
        setModelAttributesForDriverTripDetailsPage(id, modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/start/{id}")
    public ModelAndView startTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("put:/trips/drivers/start/{}", id);
        driverTripService.setStatusDrivingById(id);
        setModelAttributesForDriverTripDetailsPage(id, modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/complete/{id}")
    public ModelAndView completeTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("put:/trips/drivers/complete/{}", id);
        try {
            driverTripService.setStatusCompletedById(id);
        } catch (PaymentException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            setModelAttributesForDriverTripDetailsPage(id, modelAndView);
            log.info("trip {} wasn't completed. Cause: {}", id, e.getMessage());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/drivers/manage/" + id);
        log.info("trip {} was completed.", id);
        return modelAndView;
    }



    private void setModelAttributesForDriverTripDetailsPage(String id, ModelAndView modelAndView) {
        TripDtoForDriverPage trip = driverTripService.getDtoForDriverById(id);
        String nextPossibleAction = driverTripService.getNextActionDependingOnStatus(trip.getTripStatus());
        modelAndView.addObject("trip", trip);
        modelAndView.addObject("action", nextPossibleAction);
        modelAndView.setViewName("trips/trip-details");
    }

    private void setModelAttributesForAccountTripPages(
            ModelAndView modelAndView, PageWrapper<? extends TripDtoShortInfo> page, String subPage, SortWrapper sort) {
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("page", page);
        modelAndView.addObject("activePage", "myAccount");
        modelAndView.addObject("subPage", subPage);
        modelAndView.setViewName("trips/user-trips");
    }

    private void setModelAttributesForGetPassengerPages(
            ModelAndView modelAndView, PageWrapper<? extends TripDtoShortInfo> page, SortWrapper sort) {
        modelAndView.addObject("sort", sort);
        modelAndView.addObject("page", page);
        modelAndView.addObject("activePage", "Find passengers");
        modelAndView.setViewName("trips/find-passengers");
    }

}
