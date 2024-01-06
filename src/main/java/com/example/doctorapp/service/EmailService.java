package com.example.doctorapp.service;

import com.example.doctorapp.entity.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailWithActivationCode(String reciepentsEmail) throws MessagingException;
    void sendEmailWithResetCode(String receipentsEmail, String resetCode) throws MessagingException;
}
