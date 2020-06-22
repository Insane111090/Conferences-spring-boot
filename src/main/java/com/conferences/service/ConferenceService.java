package com.conferences.service;

import com.conferences.domain.Conference;
import com.conferences.domain.User;
import com.conferences.repository.ConferenceRepository;
import com.conferences.service.dto.ConferenceDTO;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;
    
    @Autowired
    public ConferenceService(ConferenceRepository repository) {
        this.conferenceRepository = repository;
    }
    
    public Conference createConference(ConferenceDTO conferenceDTO) {
        Conference newConference = new Conference(conferenceDTO.getTitle(),
                                                  conferenceDTO.getShortDescription(),
                                                  conferenceDTO.getLocation(),
                                                  conferenceDTO.getOrganiser(),
                                                  conferenceDTO.getContacts(),
                                                  conferenceDTO.getStartDate(),
                                                  conferenceDTO.getEndDate(),
                                                  conferenceDTO.getRegistrationStartDate(),
                                                  conferenceDTO.getRegistrationEndDate(),
                                                  conferenceDTO.getCreatedBy()
        );
        newConference.setFullDescription(conferenceDTO.getFullDescription());
        conferenceRepository.save(newConference);
        return newConference;
    }
    
    public Conference findById(final Long conferenceId) {
        return conferenceRepository.findById(conferenceId).orElseThrow(NoSuchConferenceException::new);
    }
    
    public Optional<ConferenceDTO> updateConference(ConferenceDTO conferenceDTO) {
        return Optional.of(conferenceRepository.findById(conferenceDTO.getId()))
              .filter(Optional::isPresent)
              .map(Optional::get)
              .map(conference -> {
                  conference.setTitle(conferenceDTO.getTitle());
                  conference.setShortDescription(conferenceDTO.getShortDescription());
                  conference.setFullDescription(conferenceDTO.getFullDescription());
                  conference.setOrganizer(conferenceDTO.getOrganiser());
                  conference.setContacts(conferenceDTO.getContacts());
                  conference.setConferenceStartDate(conferenceDTO.getStartDate());
                  conference.setConferenceEndDate(conferenceDTO.getEndDate());
                  conference.setConferenceRegistrationStartDate(conferenceDTO.getRegistrationStartDate());
                  conference.setConferenceRegistrationEndDate(conferenceDTO.getRegistrationEndDate());
                  conference.setCreatedBy(conferenceDTO.getCreatedBy());
                  return conference;
              })
              .map(ConferenceDTO::new);
    }
    
    public void deleteConference(String id) {
        conferenceRepository.findById(Long.parseLong(id)).ifPresent(conferenceRepository::delete);
    }
    
    @Transactional(readOnly = true)
    public List<ConferenceDTO> getAllExistingConferences(int pageNum, int rowPerPage) {
        Pageable pageable = PageRequest.of(pageNum - 1,
                                           rowPerPage,
                                           Sort.by("title").ascending()
                                          );
        return conferenceRepository.findAll(pageable).stream().map(ConferenceDTO::new).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ConferenceDTO> findConferencesByTitle(String title) {
        List<ConferenceDTO> searchResult = conferenceRepository.findByTitleIgnoreCaseContaining(title).stream()
              .map(ConferenceDTO::new)
              .collect(Collectors.toList());
        if (searchResult.size() == 0) {
            throw new SearchResultIsEmptyException();
        }
        return searchResult;
    }
    
    @Transactional(readOnly = true)
    public List<ConferenceDTO> getUserCreatedConferences(final UserDTO user,
                                                         int pageNum,
                                                         int rowPerPage) {
        Pageable pageable = PageRequest.of(pageNum - 1,
                                           rowPerPage,
                                           Sort.by("title").ascending()
                                          );
        return conferenceRepository.findByCreatedBy(user.getId()).stream().map(ConferenceDTO::new).collect(Collectors.toList());
    }
    
    public Long count() {
        return conferenceRepository.count();
    }
}
