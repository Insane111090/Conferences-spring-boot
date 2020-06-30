package com.conferences.repository;

import com.conferences.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConferenceRepository
      extends PagingAndSortingRepository<Conference, Long>, JpaRepository<Conference, Long> {
    List<Conference> findByTitleIgnoreCaseContaining(String title);
    
    List<Conference> findByCreatedBy(long userId);
}
