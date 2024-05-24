package com.example.userserviceecom.exceptions;

public class InvalidPasswordException extends  Exception{
    public InvalidPasswordException(String message){
        super(message);
    }
}
