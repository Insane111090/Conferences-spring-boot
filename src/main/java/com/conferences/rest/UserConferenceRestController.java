package com.conferences.rest;

import com.conferences.service.ConferenceService;
import com.conferences.service.UserConferenceService;
import com.conferences.service.UserService;
import com.conferences.service.dto.UserConferenceDTO;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Slf4j
@RequestMapping(value = "/userconference")
public class UserConferenceRestController {
    private static final String APPLICATION_NAME = "ConferencesBackEnd Application";
    private final UserConferenceService userConferenceService;
    private final ConferenceService conferenceService;
    private final UserService userService;
    private HttpHeaders headers;
    
    @Autowired
    public UserConferenceRestController(UserConferenceService userConferenceService,
                                        ConferenceService conferenceService,
                                        UserService userService) {
        this.userConferenceService = userConferenceService;
        this.conferenceService = conferenceService;
        this.userService = userService;
    }
    
    @PostMapping(value = "/link",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> linkUserWithConference(@RequestBody UserConferenceDTO userConferenceDTO) throws
                                                                                                            URISyntaxException {
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "Relation between User and Conference created"
                   );
        log.debug("REST request to create relation UserConference : {}",
                  userConferenceDTO
                 );
        var createdRelation = userConferenceService.create(userConferenceDTO);
        UserDTO userDTO = new UserDTO(createdRelation.getUser());
        return ResponseEntity.created(new URI("/api/userconference/link" + userDTO.getEmail()))
              .headers(headers)
              .body(userDTO);
    }
    
    @DeleteMapping(value = "/remove/{userId},{conferenceId}")
    public ResponseEntity<Void> unlinkUserWithConference(@PathVariable String userId,
                                                         @PathVariable String conferenceId) {
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "Relation between User and Conference removed"
                   );
        log.debug("REST request to remove relation UserConference : {}",
                  userId + " " + conferenceId
                 );
        userConferenceService.deleteRelation(userId, conferenceId);
        return ResponseEntity.noContent()
              .headers(headers)
              .build();
    }
}
