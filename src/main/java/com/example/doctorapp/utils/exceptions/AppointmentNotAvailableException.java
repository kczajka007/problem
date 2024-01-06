package com.example.doctorapp.utils.exceptions;

public class AppointmentNotAvailableException extends RuntimeException{
    public AppointmentNotAvailableException(String message){
        super(message);
    }
}
