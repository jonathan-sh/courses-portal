package com.courses.portal.model;

import com.courses.portal.useful.constants.CauseDescription;
import com.courses.portal.useful.constants.DetailsDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/**
 * Created by jonathan on 7/15/17.
 */
public class Validation {
    @JsonIgnore
    public Boolean status;
    public Object cause;
    public Object details;
    public HttpStatus HttpStatus;

    public void alreadyExists(String email) {
        this.status = false;
        this.cause = CauseDescription.ALREADY_EXISTS.get();
        this.details = DetailsDescription.ALREADY_EXISTS.get() + email;
        this.HttpStatus = HttpStatus.NOT_ACCEPTABLE;
    }

    public void fieldsError(String required) {
        this.status = false;
        this.cause = CauseDescription.FIELDS_ERROR.get();
        this.details = DetailsDescription.FIELDS_REQUIRED.get() + required;
        this.HttpStatus = HttpStatus.NOT_ACCEPTABLE;
    }

}
