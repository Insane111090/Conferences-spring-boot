package com.conferences.service;

public class EmailAlreadyUsedException
      extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("User with such Email is already exist");
    }
}
