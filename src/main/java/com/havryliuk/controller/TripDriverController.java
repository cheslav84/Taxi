//package com.havryliuk.controller;
//
//import com.havryliuk.dto.PageWrapper;
//import com.havryliuk.dto.trips.TripDtoForDriver;
//import com.havryliuk.dto.trips.TripDtoForDriverDetailed;
//import com.havryliuk.dto.trips.TripDtoForPassenger;
//import com.havryliuk.dto.trips.TripDtoForUser;
//import com.havryliuk.model.CarClass;
//import com.havryliuk.model.Driver;
//import com.havryliuk.model.Trip;
//import com.havryliuk.model.User;
//import com.havryliuk.service.TripService;
//import com.havryliuk.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.validation.Valid;
//import java.util.List;
//
//
//@Slf4j
//@Controller
//@RequestMapping("/trips")
//public class TripDriverController {
//
//    private static final int INITIAL_PAGE_NUMBER = 1;//todo set size in user page and cookies
//    private static final int NUMBER_OF_ITEMS_PER_PAGE = 4;
//
//    private final TripService tripService;
//    private final UserService userService;
//
//    //todoвказати параметри пагінації
//
//    @Autowired
//    public TripDriverController(TripService tripService, UserService userService) {
//        this.tripService = tripService;
//        this.userService = userService;
//    }
//
//
//    @GetMapping("/new")
//    public ModelAndView newTripPage(ModelAndView modelAndView) {
//        log.trace("newTrip page");
//        modelAndView.addObject("trip", new Trip());
//        setModelAttributesForCreatePage(modelAndView);
//        return modelAndView;
//    }
//
//
//    @PostMapping("/new")
//    public ModelAndView createTrip(@Valid Trip trip, Errors errors, ModelAndView modelAndView) {
//        log.trace("createTrip");
//        if (errors.hasErrors()) {
//            setModelAttributesForCreatePage(modelAndView);
//            return modelAndView;
//        }
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        tripService.save(trip, user);
//        modelAndView.setViewName("redirect:/trips/users/all");
//        return modelAndView;
//    }
//
//    private void setModelAttributesForCreatePage(ModelAndView modelAndView) {
//        modelAndView.addObject("carClasses", List.of(CarClass.values()));
//        modelAndView.addObject("activePage", "Get taxi");
//        modelAndView.setViewName("get-taxi");
//    }
//
//    @PreAuthorize("hasAuthority('PASSENGER')")
//    @GetMapping("/users/all")
//    public ModelAndView allUserTrips(ModelAndView modelAndView) {
//        return allUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
//    }
//
//
//    @PreAuthorize("hasAuthority('PASSENGER')")
//    @GetMapping("/users/all/{currentPageNo}/{size}")
//    public ModelAndView allUserTripsPaginated(
//                                 @PathVariable int currentPageNo,
//                                 @PathVariable int size,
//                                 ModelAndView modelAndView) {
//        log.trace("/users/all/{currentPageNo}/{size}");
//        String requestURI = "/trips/users/all";//todo think of getting from request (then will need to get rid of PathVariables)
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//todo think to reuse of code
//        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
//        Page<TripDtoForPassenger> tripsPage = tripService.getAllByUser(user, pageable);
//        PageWrapper<TripDtoForPassenger> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
//        setModelAttributesForPassengerTripPages(modelAndView, page);
//        modelAndView.addObject("subPage", "All trips");
//        return modelAndView;
//    }
//
//    @PreAuthorize("hasAuthority('PASSENGER')")
//    @GetMapping("/users/active")
//    public ModelAndView activeUserTrips(ModelAndView modelAndView) {
//        return activeUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
//    }
//
//
//    @PreAuthorize("hasAuthority('PASSENGER')")
//    @GetMapping("/users/active/{currentPageNo}/{size}")
//    public ModelAndView activeUserTripsPaginated(
//            @PathVariable int currentPageNo,
//            @PathVariable int size,
//            ModelAndView modelAndView) {
//        log.trace("/users/active/{currentPageNo}/{size}");
//        String requestURI = "/trips/users/active";
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
//        Page<TripDtoForPassenger> tripsPage = tripService.getActiveByUser(user, pageable);
//        PageWrapper<TripDtoForPassenger> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
//        setModelAttributesForPassengerTripPages(modelAndView, page);
//        modelAndView.addObject("subPage", "Active trips");
//        return modelAndView;
//    }
//
//
//    @PreAuthorize("hasAuthority('PASSENGER')")
//    @GetMapping("/users/past")
//    public ModelAndView pastUserTrips(ModelAndView modelAndView) {
//        return pastUserTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
//    }
//
//
//    @PreAuthorize("hasAuthority('PASSENGER')")
//    @GetMapping("/users/past/{currentPageNo}/{size}")
//    public ModelAndView pastUserTripsPaginated(
//            @PathVariable int currentPageNo,
//            @PathVariable int size,
//            ModelAndView modelAndView) {
//        log.trace("/users/past/{currentPageNo}/{size}");
//        String requestURI = "/trips/users/past";
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
//        Page<TripDtoForPassenger> tripsPage = tripService.getPastByUser(user, pageable);
//        PageWrapper<TripDtoForPassenger> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
//        setModelAttributesForPassengerTripPages(modelAndView, page);
//        modelAndView.addObject("subPage", "Past trips");
//        return modelAndView;
//    }
//
//    private void setModelAttributesForPassengerTripPages(
//        ModelAndView modelAndView, PageWrapper<? extends TripDtoForUser> page) {
//        modelAndView.addObject("page", page);
//        modelAndView.addObject("activePage", "myAccount");
//        modelAndView.setViewName("trips/passenger-trips");;
//
//    }
//
//    @PreAuthorize("hasAuthority('DRIVER')")
//    @GetMapping("/drivers/active")
//    public ModelAndView allActiveTrips(ModelAndView modelAndView) {
//        return allActiveTripsPaginated(INITIAL_PAGE_NUMBER, NUMBER_OF_ITEMS_PER_PAGE, modelAndView);
//    }
//
//    @PreAuthorize("hasAuthority('DRIVER')")
//    @GetMapping("/drivers/active/{currentPageNo}/{size}")
//    public ModelAndView allActiveTripsPaginated(
//            @PathVariable int currentPageNo,
//            @PathVariable int size,
//            ModelAndView modelAndView) {
//        log.trace("/drivers/active/{currentPageNo}/{size}");
//        String requestURI = "/trips/drivers/active";
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        CarClass carClass = ((Driver) user).getCar().getCarClass();
//        Pageable pageable = PageRequest.of(currentPageNo - 1, size);
//        Page<TripDtoForDriver> tripsPage = tripService.getAllNew(carClass, pageable);
//        PageWrapper<TripDtoForDriver> page = new PageWrapper<>(tripsPage, currentPageNo, size, requestURI);
//        setModelAttributesForDriverTripPages(modelAndView, page);
//        modelAndView.addObject("subPage", "Past trips");
//        return modelAndView;
//    }
//
//    private void setModelAttributesForDriverTripPages(
//            ModelAndView modelAndView, PageWrapper<? extends TripDtoForUser> page) {
//        modelAndView.addObject("page", page);
//        modelAndView.addObject("activePage", "Find passengers");
//        modelAndView.setViewName("trips/find-passengers");;
//
//    }
//
//    @PreAuthorize("hasAuthority('DRIVER')")
//    @GetMapping("/drivers/details/{id}")
//    public ModelAndView getTrip(
//            @PathVariable String id,
//            ModelAndView modelAndView) {
//        log.trace("/drivers/details/{id}");
//        TripDtoForDriverDetailed trip = tripService.getDtoById(id);
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        int passengerAge = userService.getUserAge(trip.getPassengerBirthDate());
//        modelAndView.addObject("trip", trip);
//        modelAndView.addObject("user", user);
//        modelAndView.addObject("passengerAge", passengerAge);
//        modelAndView.addObject("activePage", "Find passengers");
//        modelAndView.setViewName("trips/take-passenger");;
//
//        return modelAndView;
//    }
//
//
//    @PreAuthorize("hasAuthority('DRIVER')")
//    @PutMapping
//    public ModelAndView setDriver(TripDtoForDriverDetailed tripDto, ModelAndView modelAndView) {
//            log.trace("setDriver");
//        if (tripDto.getId() == null) {
//           throw new IllegalArgumentException("Something went wrong. Try again later.");
//        }
//        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Trip trip = tripService.getById(tripDto.getId());
//        tripService.saveDriverAndTaxiLocation(trip, user, tripDto.getTaxiLocationAddress());
//        modelAndView.setViewName("redirect:/trips/drivers/details/" + tripDto.getId());//todo change
//        return modelAndView;
//    }
//
//}
