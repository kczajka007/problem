package com.example.doctorapp.controller;

import com.example.doctorapp.dto.UserDto;
import com.example.doctorapp.entity.Appointment;
import com.example.doctorapp.repository.UserRepository;
import com.example.doctorapp.service.AppointmentService;
import com.example.doctorapp.service.UserService;
import com.example.doctorapp.service.userDetailService.DoctorAppUserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Set;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private DoctorAppUserDetailsService doctorAppUserDetailsService;
    //checking if everyghing working at the very beggining, pls do not bother with this
    @GetMapping("/get")
    public ResponseEntity<String> startingPage(){
        return new ResponseEntity<>("Hehe", HttpStatus.OK);
    }


    // displaying all users
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        UserDto savedUserDto = userService.createUser(userDto);

        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    //activate account
    @PutMapping("/activateAccount/{userId}/")
    public ResponseEntity<String> activateAccount(@PathVariable Long userId) throws NoSuchAlgorithmException {
        if (userService.activateAccount(userId)) {
            return ResponseEntity.ok("Konto zostało aktywowane.");
        } else {
            return ResponseEntity.badRequest().body("Nieprawidłowy kod aktywacyjny.");
        }
    }

    //reset password
    @PutMapping("/reset-password/{userEmail}")
    public ResponseEntity<String> resetPassword(@PathVariable String userEmail, @RequestParam String resetCode, @RequestParam String newPassword){
        if(userService.resetUserPassword(userEmail, resetCode, newPassword)){
            return ResponseEntity.ok("Hasło zostało zresetowane.");
        }else{
            return ResponseEntity.badRequest().body("Nieprawidłowy kod resetujący hasło");
        }
    }

//    @PostMapping("/log-in")
//    public ResponseEntity<?> logIn(@RequestParam String email, @RequestParam String password) {
//        try {
//            Authentication authentication = userService.authenticateUser(email, password);
//            if (authentication != null) {
//                String token = tokenService.generateToken(authentication);
//                return ResponseEntity.ok("Hello there " + email + "! Your token: " + token);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nieprawidłowy e-mail lub hasło!");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during login");
//        }
//    }

    @PostMapping("/log-in")
    public ResponseEntity<String> logIn(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Pobieranie informacji o użytkowniku
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getUsername(); // Email użytkownika
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); // Role użytkownika

            session.setAttribute("email", userEmail);
            session.setAttribute("roles", authorities.toString());

            logger.info("Użytkownik zalogowany: {} z rolami: {}", userEmail, authorities);

            return ResponseEntity.ok("Hello there " + email + "!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nieprawidłowy e-mail lub hasło!");
        }
    }

    @PostMapping("/log-out")
    public ResponseEntity<String> logOut(HttpSession session) {
        return ResponseEntity.ok("Zostałeś wylogowany.");
    }

    @PutMapping("/create-doctor/{user_id}")
    public ResponseEntity<String> createDoctor(@PathVariable @RequestParam Long user_id){
        if(userService.swapToDoctor(user_id)) {
            return ResponseEntity.ok("Użytkownik został przekonwertowany na logopedę.");
        }else {
            return ResponseEntity.badRequest().body("Przepraszamy, coś poszło nie tak. Użytkownik nie został przekonwertowany na logopedę.");
        }
    }
    @PutMapping("/change-to-user/{user_id}")
    public ResponseEntity<String> changeToUser(@PathVariable @RequestParam Long user_id){
        if(userService.swapToUser(user_id)){
            return ResponseEntity.ok("Użytkownik został przekonwertowany na zwykłego użytkownika.");
        }else{
            return ResponseEntity.badRequest().body("Przepraszamy, coś poszło nie tak. Użytkownik nie został przekonwertowany na zwykłego użytkownika.");
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpSession session) {
        // Sprawdź, czy użytkownik jest uwierzytelniony jako "ADMIN"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        logger.info("Użytkownik zalogowany z rolami: {}", authorities);

        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak uprawnień do wykonania tej operacji.");
        }

        // Odczytaj dane z sesji
        String userEmail = (String) session.getAttribute("email");
        String userRoles = (String) session.getAttribute("roles");

        // Możesz teraz użyć userEmail i userRoles do dodatkowej autoryzacji
        // Na przykład, sprawdzić, czy użytkownik jest uprawniony do usuwania innego użytkownika

        // Logika usuwania użytkownika
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
            return ResponseEntity.ok("Użytkownik został usunięty.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika o podanym ID.");
        }
    }


    @GetMapping("/doctor/{doctorId}/appointments")
    public ResponseEntity<Set<Appointment>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        Set<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}/appointments")
    public ResponseEntity<Set<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
        Set<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }
}
