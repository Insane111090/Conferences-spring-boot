package com.conferences.controller;

import com.conferences.domain.User;
import com.conferences.service.UserService;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserDTO newUser) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("MyApplicationName",
                    "A user is created with identifier " + newUser.getEmail()
                   );
        log.debug("REST request to save User : {}", newUser);
        var createdUser = userService.createUser(newUser);
        return ResponseEntity.created(new URI("/api/user/" + newUser.getEmail()))
              .headers(headers)
              .body(createdUser);
    }
}
