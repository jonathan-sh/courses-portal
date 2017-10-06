package com.courses.portal.model.dto;

import com.courses.portal.model.Course;
import com.courses.portal.model.Provider;

import java.util.List;

/**
 * Created by jonathan on 7/14/17.
 */
public class Response {
    public String token;
    public Object entity;

    public class ProviderCourse{
        public Provider provider;
        public List<Course> courses;
    }

}
