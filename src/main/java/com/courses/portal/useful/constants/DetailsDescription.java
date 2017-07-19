package com.courses.portal.useful.constants;

/**
 * Created by jonathan on 7/15/17.
 */
public enum DetailsDescription {

    FIELDS_REQUIRED("You must inform these valid fields : "),
    PASSWORD("you shall not pass"),
    ALREADY_EXISTS("A record already exists with this email : ");

    private String description;

    DetailsDescription(String label) {
        this.description = label;
    }

    public String get() {
        return this.description;
    }
}
