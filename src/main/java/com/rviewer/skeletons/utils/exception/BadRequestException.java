package com.rviewer.skeletons.utils.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(){
        super("Bad request");
    }
}