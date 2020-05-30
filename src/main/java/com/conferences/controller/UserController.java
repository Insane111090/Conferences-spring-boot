package com.conferences.controller;

import com.conferences.domain.User;
import com.conferences.repository.UserRepository;
import com.conferences.service.EmailAlreadyUsedException;
import com.conferences.service.NoSuchUserException;
import com.conferences.service.UserService;
import com.conferences.service.dto.UserDTO;
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
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final String APPLICATION_NAME = "ConferencesBackEnd Application";
    private final UserService userService;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private HttpHeaders headers;
    
    @Autowired
    public UserController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    
    /**
     * {@code POST  /user/create}  : Creates a new user.
     * <p>
     * Creates a new user if the email is not already used
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code
     * 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "A user with email " + userDTO.getEmail() + " is created"
                   );
        log.debug("REST request to save User : {}",
                  userDTO
                 );
        var createdUser = userService.createUser(userDTO);
        return ResponseEntity.created(new URI("/api/user/create" + userDTO.getEmail()))
              .headers(headers)
              .body(createdUser);
    }
    
    /**
     * {@code PUT /user/update} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     */
    @PutMapping(value = "update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        log.debug("REST request to update User: {}",
                  userDTO
                 );
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getUserId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "A User is updated with identifier " + userDTO.getEmail()
                   );
        return ResponseUtil.wrapOrNotFound(updatedUser,
                                           headers
                                          );
        
    }
    
    /**
     * {@code GET /user/find/{id}} : Finds an existing User by ID.
     *
     * @param id of the user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws NoSuchUserException {@code 400 (Bad Request)} if User does not exist
     */
    @GetMapping(value = "/find/{id}")
    public UserDTO findUserById(@PathVariable String id) {
        var user = userService.findById(Long.parseLong(id));
        return new UserDTO(user);
    }
    
    /**
     * {@code GET /user/conferences/{id} : Finds all conferences with particular User.
     *
     * @param id of the user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws NoConferenceExistedForTheUser {@code 400 (Bad Request)} if User does not have conferences.
     */
    @GetMapping(value = "/conferences/{id}")
    public ResponseEntity<Set<?>> getAllUserConferences(@PathVariable String id) {
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "List of conferences"
                   );
        final var result = userService.getUserConferences(id);
        return ResponseEntity.ok().headers(headers).body(result);
    }
    
    /**
     * {@code GET /user/authorise/{email},{password} : Authorise User by email + password.
     *
     * @param email    of the user.
     * @param password of the user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body of the user.
     * @throws NoSuchUserException {@code 400 (Bad Request)} if User does not have conferences.
     */
    @GetMapping(value = "/authorise/{email},{password}")
    public ResponseEntity<?> authoriseUser(@PathVariable String email, @PathVariable String password) {
        headers = new HttpHeaders();
        headers.add(APPLICATION_NAME,
                    "Authorised User"
                   );
        Optional<UserDTO> authorisedUser = userService.authorizeUser(email,
                                                                     password
                                                                    );
        return ResponseUtil.wrapOrNotFound(authorisedUser,
                                           headers
                                          );
    }
}
