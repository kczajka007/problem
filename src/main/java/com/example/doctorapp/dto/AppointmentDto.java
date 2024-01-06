package com.example.doctorapp.dto;

import com.example.doctorapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long appointment_id;
    private Long patientId;
    private Long doctorId;
    private User patient;
    private User doctor;
    private LocalDateTime appointmentDateAndTime;
}
