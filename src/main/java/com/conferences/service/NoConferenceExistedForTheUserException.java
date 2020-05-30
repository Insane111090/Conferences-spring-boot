package com.conferences.service;

public class NoConferenceExistedForTheUserException
      extends RuntimeException{
    public NoConferenceExistedForTheUserException(){
        super("No conference found for the user");
    }
}
