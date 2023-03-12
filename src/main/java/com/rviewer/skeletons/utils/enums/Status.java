package com.rviewer.skeletons.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    OPEN("open"),
    CLOSE("close");

    private String status;

    Status(String status){this.status = status;}

    public String getStatus(){return this.status;}
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromText(String status){
        for(Status s : Status.values()){
            if(s.getStatus().equals(status)){
                return s;
            }
        }
        throw new IllegalArgumentException();
    }
    @Override
    public String toString() {
        return this.status;
    }
}
