package com.courses.portal.model.dto;

import com.courses.portal.model.Course;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class GradeCourse {
    @Expose
    public String grade;

    @Expose
    public List<Course> courses = new ArrayList<Course>();

}
