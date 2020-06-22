package com.conferences.model;

public enum UserConferenceRole {
    SPEAKER("Speaker"), ATTENDEE("Attendee");
    
    private final String displayValue;
    
    UserConferenceRole(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
