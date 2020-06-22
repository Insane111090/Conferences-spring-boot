package com.conferences.model;

public enum Gender {
    M("Male"), F("Female");
    
    private final String displayValue;
    
    Gender(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
