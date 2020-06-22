package com.conferences.model;

public enum EducationType {
    FULLTIME("Full-Time"), PARTTIME("Part-Time");
    
    private final String displayValue;
    
    EducationType(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
