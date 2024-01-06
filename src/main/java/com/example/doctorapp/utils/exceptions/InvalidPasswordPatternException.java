package com.example.doctorapp.utils.exceptions;

public class InvalidPasswordPatternException extends RuntimeException{
    public InvalidPasswordPatternException(String message){
        super(message);
    }
}
