package com.example.doctorapp.controller;

import com.example.doctorapp.service.EmailService;
import com.example.doctorapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @PostMapping("/sendActivationCode")
    public ResponseEntity<?> sendActivationCodeToUser(@RequestParam String receipentsEmail){
        try{
            emailService.sendEmailWithActivationCode(receipentsEmail);
            return ResponseEntity.ok("Wysyłanie wiadomości e-mail powiodło się");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wysyłanie wiadomości e-mail nie powiodło się");
        }
    }

    @PostMapping("/send-reset-password-code")
    public ResponseEntity<?> sendResetPasswordCodeToUser(@RequestParam String receipentsEmail){
        try{
            userService.requestPasswordReset(receipentsEmail);
            return ResponseEntity.ok("Wysyłanie wiadomości e-mail powiodło się");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wysyłanie wiadomości e-mail nie powiodło się");
        }
    }
}
