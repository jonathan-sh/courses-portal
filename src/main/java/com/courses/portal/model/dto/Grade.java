package com.courses.portal.model.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Grade {
    @Expose
    public String description;
    @Expose
    public List<String> courses;
    @Expose
    public List<SubGrade> subGrades;
}
