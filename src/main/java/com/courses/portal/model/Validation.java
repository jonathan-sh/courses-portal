package com.courses.portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/**
 * Created by jonathan on 7/15/17.
 */
public class Validation {
    @JsonIgnore
    public Boolean status;
    public Object  cause;
    public Object  details;
    public HttpStatus HttpStatus;

    @Override
    public String toString() {
        return "Validation{" +
                "status=" + status +
                ", cause=" + cause +
                ", details=" + details +
                ", HttpStatus=" + HttpStatus +
                '}';
    }
}
