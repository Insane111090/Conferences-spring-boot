package com.conferences.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public @Data class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    
    @Column(name = "FIRST_NAME", nullable = false)
    private String userName;
    
    @Column(name = "LAST_NAME", nullable = false)
    private String userLastName;
    
    @Column(name = "THIRD_NAME")
    private String userThirdName;
    
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @Column(name = "DATE_OF_BIRTH", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date dateOfBirth;
    
    @Column(name = "GENDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "PHONE")
    private String phoneNumber;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "organisation")
    private String organisation;
    
    @Column(name = "university")
    private String university;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "education_type")
    @Enumerated(EnumType.STRING)
    private EducationType educationType;
    
    @Column(name = "education_status")
    @Enumerated(EnumType.STRING)
    private EducationStatus educationStatus;
    
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "userParent")
    private Set<UserConference> userConferences;
    
}
