package com.conferences.service;

public class NoSuchConferenceException extends RuntimeException{
    public NoSuchConferenceException(){
        super("No conference with such ID found");
    }
}
