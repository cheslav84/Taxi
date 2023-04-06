//package com.havryliuk.util.mappers;
//
//import com.havryliuk.dto.trips.TripDto;
//import com.havryliuk.dto.trips.user.UserTripDto;
//import com.havryliuk.model.CarClass;
//import com.havryliuk.model.Trip;
//import org.springframework.stereotype.Component;
//import com.havryliuk.model.User;
//
//
//@Component
//public class MapStructMapperImpl implements MapStructMapper{
//
//    @Override
//    public TripDto tripToTripDto(Trip trip) {
//        if (trip == null) {
//            return null;
//        }
//        TripDto tripDto = new TripDto();
//
//        tripDto.setId(trip.getId());
//
//        tripDto.setPassenger(userToUserTripDto(trip.getPassenger()));
//
//        tripDto.setCarClass(trip.getCarClass());
//
//
//        return tripDto;
//    }
//
//    @Override
//    public Trip tripDtoToTrip(TripDto tripDto) {
//        if (tripDto == null) {
//            return null;
//        }
//        Trip trip = new Trip();
//
//        trip.setId(tripDto.getId());
//
//        trip.setPassenger(userDtoToUser(tripDto.getPassenger()));
//
//        trip.setCarClass(tripDto.getCarClass());
//        return trip;
//
//    }
//
//    @Override
//    public UserTripDto userToUserTripDto(User user) {
//        if (user == null) {
//            return null;
//        }
//        UserTripDto userDto = new UserTripDto();
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setSurname(user.getSurname());
//        userDto.setPhone(user.getPhone());
//        return userDto;
//    }
//
//    @Override
//    public User userDtoToUser(UserTripDto userTripDto) {
//        if (userTripDto == null) {
//            return null;
//        }
//        User user = new User();
//        user.setId(userTripDto.getId());
//        user.setName(userTripDto.getName());
//        user.setSurname(userTripDto.getSurname());
//        user.setPhone(userTripDto.getPhone());
//        return user;
//    }
//
//
//}
