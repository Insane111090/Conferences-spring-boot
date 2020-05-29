package com.conferences.repository;

import com.conferences.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository
      extends JpaRepository<Conference, Long> {
}
