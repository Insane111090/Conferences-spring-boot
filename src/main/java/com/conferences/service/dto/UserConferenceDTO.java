package com.conferences.service.dto;

import com.conferences.domain.UserConference;
import com.conferences.model.UserConferenceRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class UserConferenceDTO {
    private Long userId;
    
    private Long conferenceId;
    
    private UserConferenceRole userConferenceRole;
    
    private String reportPath;
    
    private String reviewPath;
    
    private Integer accepted;
    
    public UserConferenceDTO(String userId, String conferenceId, UserConferenceRole userConferenceRole) {
        this.userId = Long.parseLong(userId);
        this.conferenceId = Long.parseLong(conferenceId);
        this.userConferenceRole = userConferenceRole;
    }
    
    public UserConferenceDTO(UserConference userConference) {
        this.userId = userConference.getUser().getUserId();
        this.conferenceId = userConference.getConference().getConferenceId();
        this.userConferenceRole = userConference.getUserConferenceRole();
        this.reportPath = userConference.getReportPath();
        this.reviewPath = userConference.getReviewPath();
        this.accepted = userConference.getReportAccepted();
    }
    
    @Override
    public String toString() {
        return "UserConferenceDTO{" +
               "userId=" + userId +
               ", conferenceId=" + conferenceId +
               ", userConferenceRole=" + userConferenceRole +
               ", reportPath=" + reportPath +
               ", reviewPath=" + reviewPath +
               ", accepted=" + accepted +
               '}';
    }
}
