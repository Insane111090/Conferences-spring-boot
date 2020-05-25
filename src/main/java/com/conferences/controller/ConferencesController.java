package com.conferences.controller;

import com.conferences.repositories.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conferences")
public class ConferencesController {
    
    @Autowired
    ConferenceRepository conferenceRepository;
    
    /*@GetMapping("/allConferences")
    public*/

}
