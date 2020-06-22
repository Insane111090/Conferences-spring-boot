package com.conferences.domain;

import com.conferences.model.EducationStatus;
import com.conferences.model.EducationType;
import com.conferences.model.Gender;
import com.conferences.model.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "users")
public @Data class User
      implements Serializable {
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    
    @Column(name = "graduation_year")
    private String graduationYear;
    
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "userParent")
    private Set<UserConference> userConferences;
    
    public User(final String email, final String password, final String userName, final String userLastName,
                final String userThirdName, final Gender gender, final Date dateBirth) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userLastName = userLastName;
        this.userThirdName = userThirdName;
        this.gender = gender;
        this.dateOfBirth = dateBirth;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return userId != null && userId.equals(((User) o).userId);
    }
    
    @Override
    public int hashCode() {
        return 31;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "userId=" + userId +
               ", userName='" + userName + '\'' +
               ", userLastName='" + userLastName + '\'' +
               ", userThirdName='" + userThirdName + '\'' +
               ", password='" + password + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               ", gender=" + gender +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", email='" + email + '\'' +
               ", organisation='" + organisation + '\'' +
               ", university='" + university + '\'' +
               ", department='" + department + '\'' +
               ", educationType=" + educationType +
               ", educationStatus=" + educationStatus +
               ", userRole=" + userRole +
               ", userConferences=" + userConferences +
               '}';
    }
}
