package com.conferences.repositories;

import com.conferences.model.UserConference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConferenceRepository extends JpaRepository<UserConference, Long> {
}
