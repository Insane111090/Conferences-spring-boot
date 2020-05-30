package com.conferences.service;

import com.conferences.domain.Conference;
import com.conferences.domain.User;
import com.conferences.domain.UserConference;
import com.conferences.repository.ConferenceRepository;
import com.conferences.repository.UserConferenceRepository;
import com.conferences.repository.UserRepository;
import com.conferences.service.dto.UserConferenceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class UserConferenceService {
    private final UserConferenceRepository userConferenceRepository;
    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;
    
    @Autowired
    public UserConferenceService(UserConferenceRepository userConferenceRepository,
                                 UserRepository userRepository,
                                 ConferenceRepository conferenceRepository) {
        this.userConferenceRepository = userConferenceRepository;
        this.userRepository = userRepository;
        this.conferenceRepository = conferenceRepository;
    }
    
    public UserConference create(UserConferenceDTO userConferenceDTO) {
        User user = userRepository.findById(userConferenceDTO.getUserId()).orElseThrow(NoSuchUserException::new);
        Conference conference = conferenceRepository
              .findById(userConferenceDTO.getConferenceId())
              .orElseThrow(NoSuchConferenceException::new);
        userConferenceRepository.findOneByUserAndConference(user,
                                                            conference
                                                           ).ifPresent(existingRow -> {
            throw new UserAlreadyHasThisConferenceException();
        });
        UserConference userConference = new UserConference(user,
                                                           conference,
                                                           userConferenceDTO.getUserConferenceRole()
        );
        userConferenceRepository.save(userConference);
        return userConference;
    }
    
    public void deleteRelation(String userId, String conferenceId) {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(NoSuchUserException::new);
        Conference conference = conferenceRepository
              .findById(Long.parseLong(conferenceId))
              .orElseThrow(NoSuchConferenceException::new);
        userConferenceRepository.findOneByUserAndConference(user,
                                                            conference
                                                           ).ifPresent(userConferenceRepository::delete);
        
    }
}
