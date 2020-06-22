package com.conferences.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "conferences")
public @Data class Conference
      implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONFERENCE_ID", nullable = false)
    private Long conferenceId;
    
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
    
    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "conferenceParent")
    private Set<UserConference> userConferences;
    
    public Conference(final String title, final String shortDescription, final String location,
                      final String owner, final String contacts, final Date startDate, final Date endDate,
                      final Date registrationStartDate, final Date registrationEndDate,
                      final Long createdBy) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.conferenceLocation = location;
        this.organizer = owner;
        this.contacts = contacts;
        this.conferenceStartDate = startDate;
        this.conferenceEndDate = endDate;
        this.conferenceRegistrationStartDate = registrationStartDate;
        this.conferenceRegistrationEndDate = registrationEndDate;
        this.createdBy = createdBy;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conference)) {
            return false;
        }
        return conferenceId != null && conferenceId.equals(((Conference) o).conferenceId);
    }
    
    @Override
    public int hashCode() {
        return 31;
    }
    
    @Override
    public String toString() {
        return "Conference{" +
               "conferenceId=" + conferenceId +
               ", title='" + title + '\'' +
               ", shortDescription='" + shortDescription + '\'' +
               ", fullDescription='" + fullDescription + '\'' +
               ", conferenceLocation='" + conferenceLocation + '\'' +
               ", organizer='" + organizer + '\'' +
               ", contacts='" + contacts + '\'' +
               ", conferenceStartDate=" + conferenceStartDate +
               ", conferenceEndDate=" + conferenceEndDate +
               ", conferenceRegistrationStartDate=" + conferenceRegistrationStartDate +
               ", conferenceRegistrationEndDate=" + conferenceRegistrationEndDate +
               ", createdBy=" + createdBy +
               ", userConferences=" + userConferences +
               '}';
    }
}
