package com.conferences.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "conferences")
public @Data class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONFERENCE_ID", nullable = false)
    private long conferenceId;
    
    @Column(name = "TITLE", nullable = false)
    private String title;
    
    @Column(name = "SHORT_DESCRIPTION", nullable = false)
    private String shortDescription;
    
    @Column(name = "FULL_DESCRIPTION")
    private String fullDescription;
    
    @Column(name = "CONF_LOCATION", nullable = false)
    private String conferenceLocation;
    
    @Column(name = "ORGANIZER", nullable = false)
    private String organizer;
    
    @Column(name = "CONTACTS", nullable = false)
    private String contacts;
    
    @Column(name = "START_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date conferenceStartDate;
    
    @Column(name = "END_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date conferenceEndDate;
    
    @Column(name = "START_REGISTRATION", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date conferenceRegistrationStartDate;
    
    @Column(name = "END_REGISTRATION", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date conferenceRegistrationEndDate;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "conferenceParent")
    private Set<UserConference> userConferences;
    
}
