package com.conferences.service.dto;

import com.conferences.domain.Conference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * A DTO representing a conference with it's users
 */
@NoArgsConstructor
public @Data class ConferenceDTO {
    private Long id;
    
    private String title;
    
    private String shortDescription;
    
    private String fullDescription;
    
    private String location;
    
    private String organiser;
    
    private String contacts;
    
    private Date startDate;
    
    private Date endDate;
    
    private Date registrationStartDate;
    
    private Date registrationEndDate;
    
    public ConferenceDTO(Conference conference){
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
               '}';
    }
}
