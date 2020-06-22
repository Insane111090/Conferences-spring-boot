package com.conferences.service.dto;

import com.conferences.domain.Conference;
import com.conferences.domain.UserConference;
import com.conferences.model.UserConferenceRole;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A DTO representing a conference with it's users
 */
@NoArgsConstructor
@Slf4j
public @Data class ConferenceDTO {
    private Long id;
    
    private String title;
    
    private String shortDescription;
    
    private String fullDescription;
    
    private String location;
    
    private String organiser;
    
    private String contacts;
    
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registrationStartDate;
    
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registrationEndDate;
    
    private UserConferenceRole userConferenceRole;
    
    private Long userConferenceRelationId;
    
    private Long createdBy;
    
    public ConferenceDTO(Conference conference) {
        this.id = conference.getConferenceId();
        this.title = conference.getTitle();
        this.shortDescription = conference.getShortDescription();
        this.fullDescription = conference.getFullDescription();
        this.location = conference.getConferenceLocation();
        this.organiser = conference.getOrganizer();
        this.contacts = conference.getContacts();
        this.startDate = conference.getConferenceStartDate();
        this.endDate = conference.getConferenceEndDate();
        this.registrationEndDate = conference.getConferenceRegistrationEndDate();
        this.registrationStartDate = conference.getConferenceRegistrationStartDate();
        if (conference.getUserConferences().size() > 0) {
            this.userConferenceRole = conference.getUserConferences().iterator().next().getUserConferenceRole();
            this.userConferenceRelationId = conference.getUserConferences().iterator().next().getUserConferenceID();
        }
        this.createdBy = conference.getCreatedBy();
        
    }
    
    @Override
    public String toString() {
        return "ConferenceDTO{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", shortDescription='" + shortDescription + '\'' +
               ", fullDescription='" + fullDescription + '\'' +
               ", location='" + location + '\'' +
               ", organiser='" + organiser + '\'' +
               ", contacts='" + contacts + '\'' +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", registrationStartDate=" + registrationStartDate +
               ", registrationEndDate=" + registrationEndDate +
               ", createdBy=" + createdBy +
               '}';
    }
}
