package com.conferences.services;

import com.conferences.exceptions.UserException;
import com.conferences.model.User;
import com.conferences.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public User createUser(final User user) throws UserException {
        try {
            userRepository.save(user);
        } //TODO: find how to properly handle exception here
        catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new UserException("User with such email already exists");
        }
        return user;
    }
    
    @Transactional
    public User findById(final Long userId) throws UserException {
        return userRepository.findById(userId).orElseThrow(() -> new UserException("No Data Found"));
    }
    
    @Transactional
    public User authorizeUser(final String email,
                              final String password) throws UserException {
        return userRepository.findUserByEmailAndPassword(email,
                                                         password
                                                        ).orElseThrow(() -> new UserException("No Such User"));
    }
    
    @Transactional
    public void updateUser(final User updatedUser) {
        userRepository.save(updatedUser);
    }
    
}
