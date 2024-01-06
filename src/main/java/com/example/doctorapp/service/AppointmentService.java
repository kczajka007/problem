package com.example.doctorapp.service;

import com.example.doctorapp.entity.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface AppointmentService {
    boolean isAppointmentAvailable(Long doctorId, LocalDateTime appointmentTime);
    boolean createAppointment(Long patientId, Long doctorId, LocalDateTime appointmentTime);
    boolean changeAppointmentDate(Long appointmentId, LocalDateTime newDate);
    Set<Appointment> getAppointmentsByDoctor(Long doctorId);
    Set<Appointment> getAppointmentsByPatient(Long patientId);
    boolean cancelAppointment(Long appointmentId);
}
