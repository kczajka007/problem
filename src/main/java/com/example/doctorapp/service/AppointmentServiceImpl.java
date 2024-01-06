package com.example.doctorapp.service;

import com.example.doctorapp.entity.Appointment;
import com.example.doctorapp.entity.User;
import com.example.doctorapp.repository.AppointmentRepository;
import com.example.doctorapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppointmentServiceImpl implements AppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Set<Appointment> getAppointmentsByDoctor(Long doctorId){
        return appointmentRepository.findByDoctorId(doctorId);
    }
    @Override
    public Set<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    @Override
    public boolean isAppointmentAvailable(Long doctorId, LocalDateTime appointmentTime) {
        return !appointmentRepository.existsByDoctorIdAndAppointmentDateAndTime(doctorId, appointmentTime);
    }

    private boolean isValidAppointmentTime(LocalDateTime appointmentTime) {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0); // Przykładowa data początkowa
        int startHour = 8;
        int endHour = 17;

        return !appointmentTime.isBefore(startDate) &&
                appointmentTime.getHour() >= startHour &&
                appointmentTime.getHour() < endHour;
    }

    @Override
    public boolean createAppointment(Long patientId, Long doctorId, LocalDateTime appointmentTime) {
        User patient = userRepository.findById(patientId).orElse(null);
        User doctor = userRepository.findById(doctorId).orElse(null);
        if(patient != null && doctor != null && isAppointmentAvailable(doctor.getUser_id(), appointmentTime)){
            if (isValidAppointmentTime(appointmentTime)) {
                Appointment appointment = new Appointment();
                appointment.setPatientId(patientId);
                appointment.setDoctorId(doctorId);
                appointment.setAppointmentDateAndTime(appointmentTime);
                appointmentRepository.save(appointment);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeAppointmentDate(Long appointmentId, LocalDateTime newDate) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);

        if (appointmentOptional.isPresent() && isValidAppointmentTime(newDate)) {
            Appointment appointment = appointmentOptional.get();
            appointment.setAppointmentDateAndTime(newDate);
            appointmentRepository.save(appointment);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean cancelAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent()) {
            appointmentRepository.deleteById(appointmentId);
            return true;
        } else {
            return false; // Wizyta nie została znaleziona
        }
    }
}
