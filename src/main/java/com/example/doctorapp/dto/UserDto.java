package com.example.doctorapp.dto;

import com.example.doctorapp.entity.Appointment;
import com.example.doctorapp.entity.Course;
import com.example.doctorapp.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long user_id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private Set<Appointment> userAppointments;
    private Set<Appointment> doctorAppointments;
    private String activationCode;
    private String activationSalt;
    private LocalDateTime activationTime;
    private boolean active;
    private String resetPasswordCode;
    private LocalDateTime passwordResetCodeExpirationTime;
    private Set<Course> courses;
}
