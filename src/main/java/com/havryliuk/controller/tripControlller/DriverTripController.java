package com.havryliuk.controller.tripControlller;

import com.havryliuk.dto.PageWrapper;
import com.havryliuk.dto.trips.*;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.*;
import com.havryliuk.service.PaymentService;
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


@Slf4j
@Controller
@RequestMapping("/trips")
public class DriverTripController {

    private static final int INITIAL_PAGE_NUMBER = 1;//todo set size in user page and cookies
    private static final int NUMBER_OF_ITEMS_PER_PAGE = 4;
    private final DriverTripService driverTripService;
//    private final PaymentService paymentService;

//    @Autowired
//    public DriverTripController(DriverTripService driverTripService, PaymentService paymentService) {
//        this.driverTripService = driverTripService;
//        this.paymentService = paymentService;
//    }

    @Autowired
    public DriverTripController(DriverTripService driverTripService) {
        this.driverTripService = driverTripService;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/available")
    public ModelAndView allActiveTrips(ModelAndView modelAndView) {
        return allActiveTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/available/{currentPageNo}/{size}")
    public ModelAndView allActiveTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/drivers/available/{}/{}", currentPageNo, size);
        String requestURI = "/trips/drivers/available";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CarClass carClass = ((Driver) user).getCar().getCarClass();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoShortInfo> tripsPage = driverTripService.getAllNew(carClass, pageable);
        PageWrapper<TripDtoShortInfo> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForGetPassengerPages(modelAndView, page);
        modelAndView.addObject("subPage", "Past trips");
        return modelAndView;
    }



    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/details/{id}")
    public ModelAndView getTrip(
            @PathVariable String id,
            ModelAndView modelAndView) {
        log.trace("/drivers/details/{}", id);
        TripDtoForDriverDetailed trip = driverTripService.getDtoById(id);
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("trip", trip);
        modelAndView.addObject("user", user);
        modelAndView.addObject("activePage", "Find passengers");
        modelAndView.setViewName("trips/take-passenger");;
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/take/{id}")
    public ModelAndView setDriver(@PathVariable String id, TripDtoForDriverDetailed tripDto,
                                  ModelAndView modelAndView) {
        log.trace("setDriver");
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Trip trip = driverTripService.getById(id);
        driverTripService.saveDriverAndTaxiLocation(trip, user, tripDto.getTaxiLocationAddress());
        modelAndView.setViewName("redirect:/trips/drivers/active");
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/all")
    public ModelAndView allDriverTrips(ModelAndView modelAndView) {
        return allDriverTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/all/{currentPageNo}/{size}")
    public ModelAndView allDriverTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/drivers/all/{}/{}", currentPageNo, size);
        String requestURI = "/trips/drivers/all";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForDriverPage> tripsPage = driverTripService.getAllByDriver(user, pageable);
        PageWrapper<TripDtoForDriverPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForAccountTripPages(modelAndView, page);
        modelAndView.addObject("subPage", "All trips");
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/active")
    public ModelAndView activeDriverTrips(ModelAndView modelAndView) {
        return activeDriverTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/active/{currentPageNo}/{size}")
    public ModelAndView activeDriverTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/drivers/active/{}/{}", currentPageNo, size);
        String requestURI = "/trips/drivers/active";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForDriverPage> tripsPage = driverTripService.getActiveByDriver(user, pageable);
        PageWrapper<TripDtoForDriverPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForAccountTripPages(modelAndView, page);
        modelAndView.addObject("subPage", "Active trips");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/past")
    public ModelAndView pastDriverTrips(ModelAndView modelAndView) {
        return pastDriverTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/past/{currentPageNo}/{size}")
    public ModelAndView pastDriverTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/drivers/past/{}/{}", currentPageNo, size);
        String requestURI = "/trips/drivers/past";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForDriverPage> tripsPage = driverTripService.getPastByDriver(user, pageable);
        PageWrapper<TripDtoForDriverPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForAccountTripPages(modelAndView, page);
        modelAndView.addObject("subPage", "Past trips");
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/drivers/manage/{id}")
    public ModelAndView tripDriverDetailsPage(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("/drivers/manage/{}", id);
        setModelAttributesForDriverTripDetailsPage(id, modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/start/{id}")
    public ModelAndView startTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("/drivers/start/{}", id);
        driverTripService.setStatusDrivingById(id);
        setModelAttributesForDriverTripDetailsPage(id, modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/complete/{id}")
    public ModelAndView completeTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("/drivers/complete/{}", id);
        try {
            driverTripService.setStatusCompletedById(id);
        } catch (PaymentException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            setModelAttributesForDriverTripDetailsPage(id, modelAndView);
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/drivers/all");
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
            ModelAndView modelAndView, PageWrapper<? extends TripDtoShortInfo> page) {
        modelAndView.addObject("page", page);
        modelAndView.addObject("activePage", "myAccount");
        modelAndView.setViewName("trips/user-trips");;
    }

    private void setModelAttributesForGetPassengerPages(
            ModelAndView modelAndView, PageWrapper<? extends TripDtoShortInfo> page) {
        modelAndView.addObject("page", page);
        modelAndView.addObject("activePage", "Find passengers");
        modelAndView.setViewName("trips/find-passengers");;
    }

}
