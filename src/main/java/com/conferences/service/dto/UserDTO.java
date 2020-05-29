package com.conferences.service.dto;

import com.conferences.domain.Conference;
import com.conferences.domain.User;
import com.conferences.domain.UserConference;
import com.conferences.model.EducationStatus;
import com.conferences.model.EducationType;
import com.conferences.model.Gender;
import com.conferences.model.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user with his conferences
 */
@NoArgsConstructor
public @Data class UserDTO {
    private Long id;
    
    @Size(max = 50)
    private String firstName;
    
    @Size(max = 50)
    private String lastName;
    
    @Size(max = 50)
    private String thirdName;
    
    @Email
    @Size(min = 5, max = 254)
    private String email;
    
    @Size(min = 5, max = 199)
    private String password;
    
    private Date birthDate;
    
    private Gender gender;
    
    @Size(max = 20)
    private String phone;
    
    private String organisation;
    
    private String university;
    
    private String universityDepartment;
    
    private EducationType educationType;
    
    private EducationStatus educationStatus;
    
    @Size(max = 4)
    private String graduatedWhen;
    
    private UserRole userRole;
    
    private Set<Conference> userConferences;
    
    public UserDTO(User user) {
        this.id = user.getUserId();
        this.firstName = user.getUserName();
        this.lastName = user.getUserLastName();
        this.thirdName = user.getUserThirdName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.birthDate = user.getDateOfBirth();
        this.gender = user.getGender();
        this.phone = user.getPhoneNumber();
        this.organisation = user.getOrganisation();
        this.university = user.getUniversity();
        this.universityDepartment = user.getDepartment();
        this.educationType = user.getEducationType();
        this.educationStatus = user.getEducationStatus();
        this.graduatedWhen = user.getGraduationYear();
        this.userRole = user.getUserRole();
        this.userConferences = user.getUserConferences().stream()
              .map(UserConference::getConference)
              .collect(Collectors.toSet());
    }
    
    @Override
    public String toString() {
        return "UserDTO{" +
               "id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", thirdName='" + thirdName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", birthDate=" + birthDate +
               ", gender=" + gender +
               ", phone='" + phone + '\'' +
               ", organisation='" + organisation + '\'' +
               ", university='" + university + '\'' +
               ", universityDepartment='" + universityDepartment + '\'' +
               ", educationType=" + educationType +
               ", educationStatus=" + educationStatus +
               ", userRole=" + userRole +
               ", userConferences=" + userConferences +
               '}';
    }
}
