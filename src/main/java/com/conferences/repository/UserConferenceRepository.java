package com.conferences.repository;

import com.conferences.domain.UserConference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConferenceRepository
      extends JpaRepository<UserConference, Long> {
}
