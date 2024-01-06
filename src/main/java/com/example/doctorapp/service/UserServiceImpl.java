package com.example.doctorapp.service;

import com.example.doctorapp.dto.UserDto;
import com.example.doctorapp.entity.User;
import com.example.doctorapp.mapper.UserMapper;
import com.example.doctorapp.repository.UserRepository;
import com.example.doctorapp.utils.ActivationCodeGen;
import com.example.doctorapp.utils.Role;
import com.example.doctorapp.utils.exceptions.EmailAlreadyExistsException;
import com.example.doctorapp.utils.exceptions.EmailNotFoundException;
import com.example.doctorapp.utils.exceptions.InvalidPasswordPatternException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailService emailService;


    @Override
    public UserDto createUser(UserDto userDto) throws NoSuchAlgorithmException, EmailAlreadyExistsException {
        User user = new User();

        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new EmailAlreadyExistsException("Konto o podanym adresie e-mail (" + userDto.getEmail() + ") już istnieje!");
        }

        //passing all data
        user.setUser_id(userDto.getUser_id());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setActive(userDto.isActive());
        user.setResetPasswordCode(null);
        user.setPasswordResetCodeExpirationTime(null);

        User savedUser = userRepository.save(user);

        //Creating random salt
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] byteSalt = new byte[16];
        secureRandom.nextBytes(byteSalt);
        String salt = Base64.getEncoder().encodeToString(byteSalt);

        //Creating activation code
        String activationCode = ActivationCodeGen.generateActivationCode(savedUser.getUser_id().toString(), salt);
        savedUser.setActivationCode(activationCode);
        savedUser.setActivationSalt(salt);
        //User has 24 hours to activate account
        savedUser.setActivationTime(LocalDateTime.now().plusHours(24));

        User updatedSavedUser = userRepository.save(savedUser);

        return userMapper.mapToUserDto(updatedSavedUser);
    }

    @Override
    public boolean activateAccount(Long userId) {

        //Declaring necessary variables
        User user = userRepository.findById(userId).orElse(null);
        boolean isUserActive = user.isActive();

        if (user != null && user.isActive()) {
            return isUserActive = false;
        }

        if (user != null && user.getActivationCode() != null && user.getActivationSalt() != null) {
            // Veryfing the activation code
            boolean isValid = ActivationCodeGen.verifyActivationCode(
                    userId.toString(),
                    user.getActivationCode(),
                    user.getActivationSalt()
            );

            if (isValid) {
                // Setting account as active
                user.setActive(true);
                userRepository.save(user);
                return isUserActive = true;
            }
        }
        return isUserActive = false;
    }

    //Generating reset code
    private String generatePasswordResetCode(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(48, random).toString(32);
    }

    public void requestPasswordReset(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new EmailNotFoundException("Nie znaleziono użytkownika z podanym adresem e-mail!");
        }

        //Creating password reset code and it's duration time
        String resetCode = generatePasswordResetCode();
        user.setResetPasswordCode(resetCode);
        user.setPasswordResetCodeExpirationTime(LocalDateTime.now().plusHours(12));
        userRepository.save(user);

        //Sending email with reset code to user with given email
        emailService.sendEmailWithResetCode(email, resetCode);
    }

    //reseting passowrd method
    public boolean resetUserPassword(String email, String resetCode, String newPassword){
        User user = userRepository.findByEmail(email);
        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).+$";

        if(user == null){
            throw new EmailNotFoundException("Nie znaleziono użytkownika z podanym adresem e-mail!");
        } else if (!newPassword.matches(passwordPattern)) {
            throw new InvalidPasswordPatternException("Hasło powinno zawierać co najmniej jedną cyfrę oraz co najmniej jeden znak specjalny");
        }

        //creating variable to determine whether reset code from the database will be valid or not
        LocalDateTime now = LocalDateTime.now();

        if(resetCode.equals(user.getResetPasswordCode()) && now.isBefore(user.getPasswordResetCodeExpirationTime())){
            user.setPassword(passwordEncoder.encode(newPassword));
            //clearing reset password code for the future incidents
            user.setResetPasswordCode(null);
            user.setPasswordResetCodeExpirationTime(null);
            userRepository.save(user);
            //returning true because reseting passowrd has been successfully made
            return true;
        }
        //returning true because reseting passowrd has not been successfully made
        return false;
    }

    @Override
    public boolean userLogIn(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new EmailNotFoundException("Nie znaleziono użytkownika z podanym adresem e-mail!");
        }

        if(passwordEncoder.matches(password, user.getPassword())){
            return true;
        }

        return false;
    }

    //changing user role to doctor
    @Override
    public boolean swapToDoctor(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            return false;
        }

        user.setRole(Role.DOCTOR);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean swapToUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            return false;
        }

        user.setRole(Role.USER);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false; // Użytkownik nie został znaleziony
        }
    }

}
