package com.conferences.service;

public class SearchResultIsEmptyException extends RuntimeException{
    public SearchResultIsEmptyException(){
        super("Nothing can be found");
    }
}
