package com.courses.portal.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SubGrade {
    @Expose
    public String description;
    @Expose
    public List<String> courses;
}
