package com.conferences.model;

public enum UserRole {
    ADMIN("Admin"), USER("User");
    
    private final String displayValue;
    
    UserRole(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
}
