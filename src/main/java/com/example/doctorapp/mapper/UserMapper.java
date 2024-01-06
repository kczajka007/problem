package com.example.doctorapp.mapper;

import com.example.doctorapp.dto.UserDto;
import com.example.doctorapp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto(
                user.getUser_id(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getUserAppointments(),
                user.getDoctorAppointments(),
                user.getActivationCode(),
                user.getActivationSalt(),
                user.getActivationTime(),
                user.isActive(),
                user.getResetPasswordCode(),
                user.getPasswordResetCodeExpirationTime(),
                user.getCourses()
        );
        return userDto;
    }

    public User mapToUser(UserDto userDto){
        User user = new User(
                userDto.getUser_id(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getUserAppointments(),
                userDto.getDoctorAppointments(),
                userDto.getActivationCode(),
                userDto.getActivationSalt(),
                userDto.getActivationTime(),
                userDto.isActive(),
                userDto.getResetPasswordCode(),
                userDto.getPasswordResetCodeExpirationTime(),
                userDto.getCourses()
        );
        return user;
    }
}
