package com.conferences.rest;

import com.conferences.domain.Conference;
import com.conferences.repository.ConferenceRepository;
import com.conferences.service.ConferenceService;
import com.conferences.service.dto.ConferenceDTO;
import com.conferences.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conference")
public class ConferenceRestController {
    private static final String APPLICATION_NAME = "ConferencesBackEnd Application";
    private final ConferenceService conferenceService;
    private final ConferenceRepository conferenceRepository;
    private final Logger log = LoggerFactory.getLogger(UserRestController.class);
    private HttpHeaders headers;
    
    @Autowired
    public ConferenceRestController(ConferenceService service, ConferenceRepository repository) {
        this.conferenceService = service;
        this.conferenceRepository = repository;
    }
    
    /**
     * {@code POST  /conference/create}  : Creates a new conference.
     * <p>
     * Creates a new conference
     *
     * @param conferenceDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Conference> createConference(@RequestBody ConferenceDTO conferenceDTO) throws URISyntaxException {
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "A conference with title " + conferenceDTO.getTitle() + " is created"
                   );
        log.debug("REST request to save Conference : {}",
                  conferenceDTO
                 );
        var createdConference = conferenceService.createConference(conferenceDTO);
        return ResponseEntity.created(new URI("/conference/create" + conferenceDTO.getTitle()))
              .headers(headers)
              .body(createdConference);
    }
    
    /**
     * {@code PUT /conference/update} : Updates an existing Conference.
     *
     * @param conferenceDTO the conference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conference.
     */
    @PutMapping(value = "update")
    public ResponseEntity<?> updateConference(@RequestBody ConferenceDTO conferenceDTO) {
        log.debug("REST request to update Conference: {}",
                  conferenceDTO
                 );
        Optional<ConferenceDTO> updatedConference = conferenceService.updateConference(conferenceDTO);
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "A Conference is updated with title " + conferenceDTO.getTitle()
                   );
        return ResponseUtil.wrapOrNotFound(updatedConference,
                                           headers
                                          );
        
    }
    
    /**
     * {@code DELETE /conference/remove/{id} : delete the Conference.
     *
     * @param id the id of the conference to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping(value = "/remove/{id}")
    public ResponseEntity<Void> deleteConference(@PathVariable String id) {
        log.debug("REST request to delete Conference: {}",
                  id
                 );
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "A Conference is deleted with id " + id
                   );
        conferenceService.deleteConference(id);
        return ResponseEntity.noContent().headers(headers).build();
    }
    
    /*@GetMapping(value = "/all")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences(Pageable pageable) {
        final Page<ConferenceDTO> page = conferenceService.getAllExistingConferences(pageable);
        headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                                                               page
                                                              );
        return new ResponseEntity<>(page.getContent(),
                                    headers,
                                    HttpStatus.OK
        );
    }*/
    
    @GetMapping(value = "/findby/{title}")
    public List<ConferenceDTO> findConferencesByTitleContains(@PathVariable String title){
        return conferenceService.findConferencesByTitle(title);
    }
}
