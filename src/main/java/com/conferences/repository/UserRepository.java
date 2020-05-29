package com.conferences.repository;

import com.conferences.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
      extends JpaRepository<User, Long> {
    
    Optional<User> findOneByEmailAndPasswordIgnoreCase(final String email, final String password);
    
    Optional<User> findOneByEmailIgnoreCase(final String email);
    
}
