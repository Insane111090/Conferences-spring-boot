package com.conferences.repositories;

import com.conferences.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
      extends JpaRepository<User, Long> {
    
    Optional<User> findUserByEmailAndPassword(final String email, final String password);
    
}
