package com.rviewer.skeletons.utils.exception;

public class DispenserDoesNotExistException extends RuntimeException {
    public DispenserDoesNotExistException(){
        super("Requested dispenser does not exist");
    }

}