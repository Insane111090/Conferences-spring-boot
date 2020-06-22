package com.conferences.model;

public enum EducationStatus {
    ENROLLEE("Enrollee"), STUDENT("Student"), GRADUATE("Graduate");
    
    private final String displayValue;
    
    EducationStatus(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
