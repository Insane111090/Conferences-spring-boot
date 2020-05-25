package com.conferences.controller;

import com.conferences.exceptions.UserException;
import com.conferences.model.User;
import com.conferences.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UsersController {
    private final UserService userService;
    
    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        User createdUser;
        try {
            createdUser = userService.createUser(newUser);
            return new ResponseEntity<>(createdUser,
                                        HttpStatus.CREATED);
        }
        catch (UserException e) {
            return new ResponseEntity<>(newUser,
                                        HttpStatus.NOT_ACCEPTABLE
            );
        }
        
    }
}
