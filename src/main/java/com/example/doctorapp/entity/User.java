package com.example.doctorapp.entity;

import com.example.doctorapp.utils.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Email(message = "Proszę wprowadzić poprawny adres e-mail")
    private String email;

    @Size(min = 8, message = "Hasło musi się składać z co najmniej 8 znaków")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$", message = "Hasło powinno zawierać co najmniej jedną cyfrę oraz co najmniej jeden znak specjalny")
    private String password;

    // to determine if it's an admin or standard user
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "patient")
    @Column(name = "appointments")
    @JsonManagedReference("user-patient")
    private Set<Appointment> userAppointments;

    @OneToMany(mappedBy = "doctor")
    @Column(name = "doctor_appointments")
    @JsonManagedReference("user-doctor")
    private Set<Appointment> doctorAppointments;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "activation_salt")
    private String activationSalt;

    @Column(name = "time_for_activation")
    private LocalDateTime activationTime;

    // to check whether this user acc is active or not
    private boolean active;

    @Column(name = "reset_password_code")
    private String resetPasswordCode;

    @Column(name = "time_for_password_reset")
    private LocalDateTime passwordResetCodeExpirationTime;

    @ManyToMany(mappedBy = "enrolledUsers")
    private Set<Course> courses;


}
