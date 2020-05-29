package com.conferences.service;

import com.conferences.domain.User;
import com.conferences.repository.UserRepository;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Transactional
    public User createUser(UserDTO userDTO) {
        userRepository.findOneByEmailAndPasswordIgnoreCase(userDTO.getEmail(),
                                                           userDTO.getPassword()
                                                          ).ifPresent(existingUser -> {
            throw new EmailAlreadyUsedException();
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
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
    
    @Transactional
    public User findById(final Long userId) throws EmailAlreadyUsedException {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No Data Found"));
    }
    
    @Transactional
    public User authorizeUser(final String email,
                              final String password) throws EmailAlreadyUsedException {
        return userRepository
              .findOneByEmailAndPasswordIgnoreCase(email,
                                                   password
                                                  )
              .orElseThrow(() -> new RuntimeException("No Such User"));
    }
    
    @Transactional
    public void updateUser(final User updatedUser) {
        userRepository.save(updatedUser);
    }
    
}
