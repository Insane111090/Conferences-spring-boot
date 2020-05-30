package com.conferences.service.dto;

import com.conferences.model.UserConferenceRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class UserConferenceDTO {
    private Long userId;
    
    private Long conferenceId;
    
    private UserConferenceRole userConferenceRole;
    
    public UserConferenceDTO(String userId, String conferenceId, UserConferenceRole userConferenceRole) {
        this.userId = Long.parseLong(userId);
        this.conferenceId = Long.parseLong(conferenceId);
        this.userConferenceRole = userConferenceRole;
    }
    
    @Override
    public String toString() {
        return "UserConferenceDTO{" +
               "userId=" + userId +
               ", conferenceId=" + conferenceId +
               ", userConferenceRole=" + userConferenceRole +
               '}';
    }
}
