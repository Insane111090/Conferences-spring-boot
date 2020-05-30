package com.conferences.repository;

import com.conferences.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceRepository
      extends JpaRepository<Conference, Long> {
    List<Conference> findByTitleIgnoreCaseContaining(String title);
}
