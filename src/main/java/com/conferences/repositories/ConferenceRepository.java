package com.conferences.repositories;

import com.conferences.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository
      extends JpaRepository<Conference, Long> {
}
