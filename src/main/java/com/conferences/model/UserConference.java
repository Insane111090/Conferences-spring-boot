package com.conferences.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USERS_CONFERENCES")
public @Data class UserConference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_CONFERENCE_ID")
    private Long userConferenceID;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference(value = "userParent")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "CONFERENCE_ID")
    @JsonBackReference(value = "conferenceParent")
    private Conference conference;
    
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserConferenceRole userConferenceRole;
}
