package com.courses.portal.useful.constants;

/**
 * Created by jonathan on 7/15/17.
 */
public enum CauseDescription {

    FIELDS_ERROR("Fields error"),
    ALREADY_EXISTS("Record already exists");

    private String description;

    CauseDescription(String label) {
        this.description = label;
    }

    public String get() {
        return this.description;
    }
}
