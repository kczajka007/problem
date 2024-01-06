package com.example.doctorapp.service;

import com.example.doctorapp.dto.UserDto;
import com.example.doctorapp.utils.exceptions.EmailAlreadyExistsException;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;

import java.security.NoSuchAlgorithmException;

public interface UserService{
    UserDto createUser(UserDto userDto) throws NoSuchAlgorithmException, EmailAlreadyExistsException;
    boolean activateAccount(Long userId) throws NoSuchAlgorithmException;
    void requestPasswordReset(String email) throws MessagingException;
    boolean resetUserPassword(String email, String resetCode, String newPassword);
    boolean userLogIn(String email, String password);
    boolean swapToDoctor(Long userId);
    boolean swapToUser(Long userId);
    boolean deleteUser(Long userId);
}
