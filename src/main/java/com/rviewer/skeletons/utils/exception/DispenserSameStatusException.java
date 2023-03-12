package com.rviewer.skeletons.utils.exception;
public class DispenserSameStatusException extends RuntimeException{
    public DispenserSameStatusException() {
        super("Dispenser is already opened/closed");
    }
}
