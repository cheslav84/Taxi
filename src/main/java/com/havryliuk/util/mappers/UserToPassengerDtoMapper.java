//package com.havryliuk.util.mappers;
//
//import com.havryliuk.dto.trips.user.UserTripDto;
//import com.havryliuk.model.User;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class UserToPassengerDtoMapper {
//
//
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
//
//
//}
