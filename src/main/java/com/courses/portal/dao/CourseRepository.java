package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Course;

import java.util.List;


/**
 * Created by jonathan on 7/15/17.
 */
public class CourseRepository extends MongoCrud {
    public CourseRepository(String collection, Class clazz) {
        super(collection, clazz);
    }


    public List<Course> findAll() {
        List<Course> courses = super.readAll();
        courses.forEach(Course::treatmentForResponse);
        return courses;
    }
}
