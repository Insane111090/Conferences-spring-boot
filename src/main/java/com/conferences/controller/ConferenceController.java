package com.conferences.controller;

import com.conferences.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {
    
    @Autowired
    ConferenceRepository conferenceRepository;
    
    /*@GetMapping("/allConferences")
    public*/

}
