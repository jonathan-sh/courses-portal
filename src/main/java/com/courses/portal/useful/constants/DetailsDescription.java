package com.courses.portal.useful.constants;

/**
 * Created by jonathan on 7/15/17.
 */
public enum DetailsDescription {

    FIELDS_REQUIRED("You must inform these valid fields : "),
    PASSWORD("you shall not pass"),
    ALREADY_EXISTS("A record already exists with this email : "),
    NOT_FOUND("Record not found with this email : "),
    NOT_CONTAINS_ENTITY("Not contains entity valid");

    private String description;

    DetailsDescription(String label) {
        this.description = label;
    }

    public String get() {
        return this.description;
    }
}
