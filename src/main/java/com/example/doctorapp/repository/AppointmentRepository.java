package com.example.doctorapp.repository;

import com.example.doctorapp.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateAndTime = :dateTime")
    boolean existsByDoctorIdAndAppointmentDateAndTime(@Param("doctorId") Long doctorId, @Param("dateTime") LocalDateTime dateTime);
    Set<Appointment> findByDoctorId(Long doctorId);
    Set<Appointment> findByPatientId(Long patientId);
}
