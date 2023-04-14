package com.havryliuk.controller.tripControlller;

import com.havryliuk.dto.PageWrapper;
import com.havryliuk.dto.SortWrapper;
import com.havryliuk.dto.trips.*;
import com.havryliuk.model.*;

import com.havryliuk.service.PaymentService;
import com.havryliuk.service.tripService.PassengerTripService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/trips")
public class PassengerTripController {

    private static final String INITIAL_PAGE_NUMBER = "1";//todo set size in user page and cookies
    private static final String NUMBER_OF_ITEMS_PER_PAGE = "4";
    private final PassengerTripService passengerTripService;
    private final PaymentService paymentService;


    @Autowired
    public PassengerTripController(PassengerTripService passengerTripService, PaymentService paymentService) {
        this.passengerTripService = passengerTripService;
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/new")
    public ModelAndView newTripPage(ModelAndView modelAndView) {
        log.trace("newTrip page");
        modelAndView.addObject("trip", new Trip());
        setModelAttributesForCreatePage(modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @PostMapping("/new")
    public ModelAndView createTrip(@Valid Trip trip, Errors errors, ModelAndView modelAndView) {
        log.trace("createTrip");
        if (errors.hasErrors()) {
            setModelAttributesForCreatePage(modelAndView);
            return modelAndView;
        }
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        passengerTripService.save(trip, user);
        modelAndView.setViewName("redirect:/trips/passengers/all");
        return modelAndView;
    }

    private void setModelAttributesForCreatePage(ModelAndView modelAndView) {
        Iterable<Tariffs> tariffs = paymentService.findAll();
        modelAndView.addObject("tariffs", tariffs);
        modelAndView.addObject("carClasses", List.of(CarClass.values()));
        modelAndView.addObject("activePage", "Get taxi");
        modelAndView.setViewName("get-taxi");
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/all")
    public ModelAndView allUserTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,asc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace(requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        Page<TripDtoForPassengerPage> tripsPage = passengerTripService.getAllByPassenger(user, pageable);
        PageWrapper<TripDtoForPassengerPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        String subPage = "All trips";
        setModelAttributesForAccountTripPages(modelAndView, page, subPage, sort);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/active")
    public ModelAndView activeUserTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,asc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace(requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        Page<TripDtoForPassengerPage> tripsPage = passengerTripService.getActiveByPassenger(user, pageable);
        PageWrapper<TripDtoForPassengerPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        String subPage = "Active trips";
        setModelAttributesForAccountTripPages(modelAndView, page, subPage, sort);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/past")
    public ModelAndView pastUserTripsPaginated(
            ModelAndView modelAndView,
            HttpServletRequest request,
            @RequestParam(defaultValue = INITIAL_PAGE_NUMBER) int currentPageNo,
            @RequestParam(defaultValue = NUMBER_OF_ITEMS_PER_PAGE) int size,
            @RequestParam(defaultValue = "departureDateTime,asc") String[] sorting) {

        String requestURI = request.getRequestURI();
        log.trace(requestURI);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SortWrapper sort = new SortWrapper(sorting);
        Pageable pageable = PageRequest.of(currentPageNo - 1, size, sort.getSortOrders());
        Page<TripDtoForPassengerPage> tripsPage = passengerTripService.getPastByPassenger(user, pageable);
        PageWrapper<TripDtoForPassengerPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        String subPage = "Past trips";
        setModelAttributesForAccountTripPages(modelAndView, page, subPage, sort);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/manage/{id}")
    public ModelAndView tripPassengerDetailsPage(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("/passengers/manage/{}", id);
        setModelAttributesForPassengerTripDetailsPage(id, modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @DeleteMapping ("/{id}")
    public ModelAndView deleteTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("(delete)/{}", id);
        try {
            passengerTripService.deleteTrip(id);
        } catch (UnsupportedOperationException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            setModelAttributesForPassengerTripDetailsPage(id, modelAndView);
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/passengers/all");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping ("/passengers/update-page/{id}")
    public ModelAndView updatePageTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("/passengers/update-page/{}", id);
        setAttributesForUpdatePage(id, modelAndView);
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @PutMapping ("/update")
    public ModelAndView updateTrip(@Valid TripDtoForPassengerUpdate trip, Errors errors, ModelAndView modelAndView) {
        log.trace("update/");
        System.err.println(trip);
        if (errors.hasErrors()) {
            setAttributesForUpdatePage(trip.getId(), modelAndView);
            log.trace("Trip data is invalid.");
            return modelAndView;
        }
        try {
            passengerTripService.updateTrip(trip);
            log.trace("Trip was successfully updated.");
        } catch (UnsupportedOperationException e) {
            setAttributesForUpdatePage(trip.getId(), modelAndView);
            modelAndView.addObject("errorMessage", e.getMessage());
            log.trace("Trip wasn't updated. {}", e.getMessage());
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/passengers/update-page/" + trip.getId());
        return modelAndView;
    }


    private void setAttributesForUpdatePage(String id, ModelAndView modelAndView) {
        TripDtoForPassengerUpdate trip = passengerTripService.getTripDtoForUserUpdateById(id);
        Iterable<Tariffs> tariffs = paymentService.findAll();
        modelAndView.addObject("tariffs", tariffs);
        modelAndView.addObject("carClasses", List.of(CarClass.values()));
        modelAndView.addObject("trip", trip);
        modelAndView.setViewName("trips/update-trip");
    }



    private void setModelAttributesForPassengerTripDetailsPage(String id, ModelAndView modelAndView) {
        TripDtoForPassengerPage trip = passengerTripService.getDtoFoPassengerById(id);
        modelAndView.addObject("trip", trip);
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


}
