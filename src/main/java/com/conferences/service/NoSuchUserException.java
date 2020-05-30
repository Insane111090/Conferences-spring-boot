package com.conferences.service;

public class NoSuchUserException
      extends RuntimeException {
    public NoSuchUserException() {
        super("No User found");
    }
}
