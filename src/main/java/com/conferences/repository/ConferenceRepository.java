package com.conferences.repository;

import com.conferences.domain.Conference;
import com.conferences.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConferenceRepository
      extends PagingAndSortingRepository<Conference, Long>, JpaRepository<Conference, Long> {
    List<Conference> findByTitleIgnoreCaseContaining(String title);
    
    List<Conference> findByCreatedBy(long userId);
}
