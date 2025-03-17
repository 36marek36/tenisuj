package com.example.tenisuj.model.enums;

import lombok.Getter;

@Getter
public enum Location {
    Court1("kurt1"),
    Court2("kurt2"),
    Court3("kurt3");

    private final String location;

    Location(String location) {
        this.location = location;
    }
}
