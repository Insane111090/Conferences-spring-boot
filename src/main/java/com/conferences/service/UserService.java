package com.conferences.service;

import com.conferences.domain.User;
import com.conferences.repository.UserRepository;
import com.conferences.service.dto.ConferenceDTO;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(UserDTO userDTO) {
        userRepository.findOneByEmailAndPasswordIgnoreCase(userDTO.getEmail().toLowerCase(),
                                                           userDTO.getPassword().toLowerCase()
                                                          ).ifPresent(existingUser -> {
            throw new EmailAlreadyUsedException();
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail().toLowerCase()).ifPresent(existingUser -> {
            throw new EmailAlreadyUsedException();
        });
        
        User newUser = new User(userDTO.getEmail(),
                                userDTO.getPassword(),
                                userDTO.getFirstName(),
                                userDTO.getLastName(),
                                userDTO.getThirdName(),
                                userDTO.getGender(),
                                userDTO.getBirthDate()
        );
        newUser.setPhoneNumber(userDTO.getPhone());
        newUser.setOrganisation(userDTO.getOrganisation());
        newUser.setDepartment(userDTO.getUniversityDepartment());
        newUser.setEducationType(userDTO.getEducationType());
        newUser.setEducationStatus(userDTO.getEducationStatus());
        newUser.setUniversity(userDTO.getUniversity());
        newUser.setGraduationYear(userDTO.getGraduatedWhen());
        newUser.setUserRole(userDTO.getUserRole());
        userRepository.save(newUser);
        return newUser;
    }
    
    @Transactional(readOnly = true)
    public User findById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserDTO> authorizeUser(final String email,
                                           final String password) {
        return Optional.of(userRepository
                                 .findOneByEmailAndPasswordIgnoreCase(email,
                                                                      password
                                                                     )
                                 .map(UserDTO::new))
              .orElseThrow(NoSuchUserException::new);
    }
    
    @Transactional(readOnly = true)
    public Set<? extends ConferenceDTO> getUserConferences(final String userId) {
        UserDTO userDTO = new UserDTO(findById(Long.parseLong(userId)));
        if (userDTO.getUserConferencesDTO().size() == 0) {
            throw new NoConferenceExistedForTheUserException();
        }
        return userDTO.getUserConferencesDTO();
    }
    
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId()))
              .filter(Optional::isPresent)
              .map(Optional::get)
              .map(user -> {
                  user.setUserName(userDTO.getFirstName());
                  user.setUserLastName(userDTO.getLastName());
                  user.setUserThirdName(userDTO.getThirdName());
                  if (userDTO.getEmail() != null) {
                      user.setEmail(userDTO.getEmail());
                  }
                  if (userDTO.getPassword() != null) {
                      user.setPassword(userDTO.getPassword());
                  }
                  user.setGender(userDTO.getGender());
                  user.setDateOfBirth(userDTO.getBirthDate());
                  user.setUniversity(userDTO.getUniversity());
                  user.setOrganisation(userDTO.getOrganisation());
                  user.setDepartment(userDTO.getUniversityDepartment());
                  user.setEducationType(userDTO.getEducationType());
                  user.setEducationStatus(userDTO.getEducationStatus());
                  user.setGraduationYear(userDTO.getGraduatedWhen());
                  return user;
              })
              .map(UserDTO::new);
    }
    
}
