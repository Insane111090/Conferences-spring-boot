package com.conferences.model;

public enum ReportStatus {
    UNDER_REVIEW("Under Review"), ACCEPTED("Accepted"), REJECTED("Rejected");
    
    private final String displayValue;
    
    ReportStatus(String displayValue){
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue(){
        return displayValue;
    }
}
