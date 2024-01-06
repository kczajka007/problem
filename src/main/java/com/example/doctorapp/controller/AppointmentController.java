package com.example.doctorapp.controller;

import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.entity.Appointment;
import com.example.doctorapp.entity.User;
import com.example.doctorapp.repository.UserRepository;
import com.example.doctorapp.service.AppointmentService;
import com.example.doctorapp.utils.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentDto appointmentDto) {

        User patient = userRepository.findById(appointmentDto.getPatientId()).orElse(null);
        User doctor = userRepository.findById(appointmentDto.getDoctorId()).orElse(null);

        if(patient != null && doctor != null){
            LocalDateTime appointmentTime = appointmentDto.getAppointmentDateAndTime();

            if(appointmentService.createAppointment(patient.getUser_id(), doctor.getUser_id(), appointmentTime)){
                return ResponseEntity.ok("Wizyta została zarezerwowana.");
            } else {
                return ResponseEntity.badRequest().body("Wizyta w danym terminie jest niedostępna.");
            }
        } else {
            return ResponseEntity.badRequest().body("Nie można znaleźć pacjenta lub lekarza.");
        }
    }

    @PutMapping("/change-date/{appointmentId}")
    public ResponseEntity<?> changeAppointmentDate(@PathVariable Long appointmentId,
                                                   @RequestParam("newDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDate,
                                                   HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            throw new UnauthorizedException("Musisz być zalogowany, aby zmienić datę wizyty.");
        }

        boolean isChanged = appointmentService.changeAppointmentDate(appointmentId, newDate);
        if (isChanged) {
            return ResponseEntity.ok("Data wizyty została zmieniona na: " + newDate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono wizyty o podanym ID");
        }
    }

    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            throw new UnauthorizedException("Musisz być zalogowany, aby anulować wizytę.");
        }
        boolean isCancelled = appointmentService.cancelAppointment(appointmentId);
        if (isCancelled) {
            return ResponseEntity.ok("Wizyta została anulowana.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono takiej wizyty.");
        }
    }
}
