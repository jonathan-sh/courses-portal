package com.courses.portal.model.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by jonathan on 7/15/17.
 */
public class Exam {
    @Expose
    public String name;
    @Expose
    public List<Question> questions;
    @Expose
    public Boolean random;
    @Expose
    public Boolean status;

}
