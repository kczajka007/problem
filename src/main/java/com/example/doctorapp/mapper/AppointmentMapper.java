package com.example.doctorapp.mapper;

import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDto mapToAppointmentDto(Appointment appointment){
        AppointmentDto appointmentDto = new AppointmentDto(
                appointment.getAppointment_id(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getPatient(),
                appointment.getDoctor(),
                appointment.getAppointmentDateAndTime()
        );
        return appointmentDto;
    }

    public Appointment mapToAppointment(AppointmentDto appointmentDto){
        Appointment appointment = new Appointment(
                appointmentDto.getAppointment_id(),
                appointmentDto.getPatientId(),
                appointmentDto.getDoctorId(),
                appointmentDto.getPatient(),
                appointmentDto.getDoctor(),
                appointmentDto.getAppointmentDateAndTime()
        );
        return appointment;
    }
}
