package com.havryliuk.controller;

import com.havryliuk.dto.PageWrapper;
import com.havryliuk.dto.trips.*;
import com.havryliuk.exceptions.PaymentException;
import com.havryliuk.model.CarClass;
import com.havryliuk.model.Trip;
import com.havryliuk.model.*;

import com.havryliuk.service.PaymentService;
import com.havryliuk.service.tripServices.TripService;
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

    private static final int INITIAL_PAGE_NUMBER = 1;//todo set size in user page and cookies
    private static final int NUMBER_OF_ITEMS_PER_PAGE = 4;
    private final TripService tripService;
    private final PaymentService paymentService;


    @Autowired
    public TripController(TripService tripService, PaymentService paymentService) {
        this.tripService = tripService;
        this.paymentService = paymentService;
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
    public ModelAndView allUserTrips(ModelAndView modelAndView) {
        return allUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/all/{currentPageNo}/{size}")
    public ModelAndView allUserTripsPaginated(
                                 @PathVariable int currentPageNo,
                                 @PathVariable int size,
                                 ModelAndView modelAndView) {
        log.trace("/passengers/all/{}/{}", currentPageNo, size);
        String requestURI = "/trips/passengers/all";//todo think of getting from request (then will need to get rid of PathVariables)
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//todo think to reuse of code
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForPassengerPage> tripsPage = tripService.getAllByPassenger(user, pageable);
        PageWrapper<TripDtoForPassengerPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForAccountTripPages(modelAndView, page);
        modelAndView.addObject("subPage", "All trips");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/active")
    public ModelAndView activeUserTrips(ModelAndView modelAndView) {
        return activeUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/active/{currentPageNo}/{size}")
    public ModelAndView activeUserTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/passengers/active/{}/{}", currentPageNo, size);
        String requestURI = "/trips/passengers/active";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForPassengerPage> tripsPage = tripService.getActiveByPassenger(user, pageable);
        PageWrapper<TripDtoForPassengerPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForAccountTripPages(modelAndView, page);
        modelAndView.addObject("subPage", "Active trips");
        return modelAndView;
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/past")
    public ModelAndView pastUserTrips(ModelAndView modelAndView) {
        return pastUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
    }


    @PreAuthorize("hasAuthority('PASSENGER')")
    @GetMapping("/passengers/past/{currentPageNo}/{size}")
    public ModelAndView pastUserTripsPaginated(
            @PathVariable int currentPageNo,
            @PathVariable int size,
            ModelAndView modelAndView) {
        log.trace("/passengers/past/{}/{}", currentPageNo, size);
        String requestURI = "/trips/passengers/past";
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
        Page<TripDtoForPassengerPage> tripsPage = tripService.getPastByPassenger(user, pageable);
        PageWrapper<TripDtoForPassengerPage> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
        setModelAttributesForAccountTripPages(modelAndView, page);
        modelAndView.addObject("subPage", "Past trips");
        return modelAndView;
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
        Page<TripDtoShortInfo> tripsPage = tripService.getAllNew(carClass, pageable);
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
        TripDtoForDriverDetailed trip = tripService.getDtoById(id);
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
        Trip trip = tripService.getById(id);
        tripService.saveDriverAndTaxiLocation(trip, user, tripDto.getTaxiLocationAddress());
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
        Page<TripDtoForDriverPage> tripsPage = tripService.getAllByDriver(user, pageable);
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
        Page<TripDtoForDriverPage> tripsPage = tripService.getActiveByDriver(user, pageable);
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
        Page<TripDtoForDriverPage> tripsPage = tripService.getPastByDriver(user, pageable);
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
        tripService.setStatusDrivingById(id);
        setModelAttributesForDriverTripDetailsPage(id, modelAndView);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @PutMapping ("/drivers/complete/{id}")
    public ModelAndView completeTrip(@PathVariable String id, ModelAndView modelAndView) {
        log.trace("/drivers/complete/{}", id);
        try {
            tripService.setStatusCompletedById(id);
        } catch (PaymentException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            setModelAttributesForDriverTripDetailsPage(id, modelAndView);
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/trips/drivers/all");
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
            tripService.deleteTrip(id);
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
            tripService.updateTrip(trip);
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
        TripDtoForPassengerUpdate trip = tripService.getTripDtoForUserUpdateById(id);
        Iterable<Tariffs> tariffs = paymentService.findAll();
        List<CarClass> carClasses =  List.of(CarClass.values());
//        int currentCarClass =  carClasses.indexOf(trip.getCarClass());
        modelAndView.addObject("tariffs", tariffs);
        modelAndView.addObject("carClasses", carClasses);
//        modelAndView.addObject("currentCarClass", currentCarClass);
        modelAndView.addObject("trip", trip);
        modelAndView.setViewName("trips/update-trip");
    }



    private void setModelAttributesForPassengerTripDetailsPage(String id, ModelAndView modelAndView) {
        TripDtoForPassengerPage trip = tripService.getDtoFoPassengerById(id);
        modelAndView.addObject("trip", trip);
        modelAndView.setViewName("trips/trip-details");
    }

    private void setModelAttributesForDriverTripDetailsPage(String id, ModelAndView modelAndView) {
        TripDtoForDriverPage trip = tripService.getDtoForDriverById(id);
        String nextPossibleAction = tripService.getNextActionDependingOnStatus(trip.getTripStatus());
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
