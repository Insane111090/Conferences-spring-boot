package com.conferences.controller;

import com.conferences.domain.User;
import com.conferences.repository.UserRepository;
import com.conferences.service.NoSuchUserException;
import com.conferences.service.UserService;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    @Value("${msg.index.title}")
    private String indexTitle;
    @Value("${msg.authorise.title}")
    private String authoriseTitle;
    @Value("${msg.cabinet.title}")
    private String userCabinetTitle;
    @Value("${msg.edituser.title}")
    private String userEditTitle;
    @Value("${msg.adduser.title}")
    private String userAddTitle;
    
    public UserController(UserService userService,
                          UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    
    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("title",
                           indexTitle
                          );
        return "index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(Model model) {
        model.addAttribute("title",
                           authoriseTitle
                          );
        UserDTO user = new UserDTO();
        model.addAttribute("user",
                           user
                          );
        return "authorise-user";
    }
    
    /**
     * {@code GET /user/authorise : Authorise User by email + password.
     *
     * @param email    of the user.
     * @param password of the user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body of the user.
     * @throws NoSuchUserException {@code 400 (Bad Request)} if User does not have conferences.
     */
    @GetMapping(value = "/users/authorise")
    public String authoriseUser(@RequestParam String email,
                                @RequestParam String password,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<UserDTO> authorisedUser = userService.authorizeUser(email,
                                                                     password
                                                                    );
        if (authorisedUser.isPresent()) {
            model.addAttribute("user",
                               authorisedUser
                              );
        }
        else {
            model.addAttribute("user",
                               authorisedUser
                              );
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 new NoSuchUserException().getMessage()
                                                );
            
            return "redirect:/login";
        }
        return String.format("redirect:/users/%s/show",
                             authorisedUser.get().getId()
                            );
    }
    
    @GetMapping(value = "/users/{userId}/show")
    public String showUserInfo(@PathVariable String userId,
                               Model model
                              ) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("title",
                           userCabinetTitle
                          );
        return "user-cabinet";
    }
    
    @GetMapping(value = "users/{userId}/edit")
    public String showEditUserPage(@PathVariable String userId,
                                   Model model) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("title",
                           userEditTitle
                          );
        return "update-user";
    }
    
    @PostMapping(value = "users/{userId}/edit")
    public String updateUserInfo(Model model,
                                 @PathVariable String userId,
                                 @ModelAttribute("user") UserDTO user) {
        log.debug("date birth: {}",
                  user.getBirthDate()
                 );
        user.setId(Long.parseLong(userId));
        log.debug("User ID: {}",
                  user.getId()
                 );
        Optional<UserDTO> updatedUser = userService.updateUser(user);
        model.addAttribute("user",
                           updatedUser
                          );
        return String.format("redirect:/users/%s/show",
                             updatedUser.get().getId()
                            );
    }
    
    @GetMapping(value = "/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("title",
                           userCabinetTitle
                          );
        UserDTO user = new UserDTO();
        model.addAttribute("user",
                           user
                          );
        return "add-user";
    }
    
    @PostMapping(value = "users/add")
    public String addNewUser(Model model,
                             @ModelAttribute("user") UserDTO user,
                             RedirectAttributes redirectAttributes) {
        if (user.getFirstName().isEmpty()||
            user.getLastName().isEmpty() ||
            user.getEmail().isEmpty() ||
            user.getPassword().isEmpty() ||
            user.getPhone().isEmpty() ||
            user.getUniversity().isEmpty()
        ) {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 "Mandatory Fields must not be empty"
                                                );
            return "redirect:/register";
        }
        
        try {
            User createdUser = userService.createUser(user);
            model.addAttribute("user",
                               user
                              );
            return String.format("redirect:/users/%s/show",
                                 createdUser.getUserId()
                                );
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 e.getMessage()
                                                );
            return "redirect:/register";
        }
    }
}
