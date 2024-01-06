package com.example.doctorapp.service;

import com.example.doctorapp.controller.EmailController;
import com.example.doctorapp.entity.User;
import com.example.doctorapp.repository.UserRepository;
import com.example.doctorapp.utils.exceptions.EmailNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendEmailWithActivationCode(String reciepentsEmail) throws MessagingException {
        //getting specific user by his email address
        User user = userRepository.findByEmail(reciepentsEmail);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        //setting the receipent of the message and it's conent
        helper.setFrom("doctorappauthcodesender@gmail.com");
        helper.setTo(reciepentsEmail);
        helper.setSubject("Kod aktywacyjny");
        helper.setText("Twój kod aktywacyjny to: " + user.getActivationCode(), true);

        //sending email
        javaMailSender.send(message);
    }

    @Override
    public void sendEmailWithResetCode(String receipentsEmail, String resetCode) throws MessagingException {
        //getting specific user by his email address
        User user = userRepository.findByEmail(receipentsEmail);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        //setting the receipent of the message and it's conent
        helper.setFrom("doctorappauthcodesender@gmail.com");
        helper.setTo(receipentsEmail);
        helper.setSubject("Reset Hasłą");
        helper.setText("Twój kod resetujący hasło to: " + user.getResetPasswordCode(), true);

        //sending email
        javaMailSender.send(message);
    }


}
