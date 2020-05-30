package com.conferences.service;

public class UserAlreadyHasThisConferenceException extends RuntimeException{
    public UserAlreadyHasThisConferenceException(){
        super("This User already has this Conference with the same Role");
    }
}
