package com.conferences.repository;

import com.conferences.domain.Conference;
import com.conferences.domain.User;
import com.conferences.domain.UserConference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConferenceRepository
      extends JpaRepository<UserConference, Long> {
    
    Optional<UserConference> findOneByUserAndConference(final User user,
                                                        final Conference conference);
}
