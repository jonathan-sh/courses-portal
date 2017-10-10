package com.courses.portal.dao;

import com.courses.portal.dao.mongoDB.MongoCrud;
import com.courses.portal.model.Course;

import java.util.List;
import java.util.stream.Collectors;


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

    public Course findById(String id) {
        List<Course> courses = findAll();
        Course course = new Course();
        try
        {
            course = courses.stream()
                    .filter(item -> item._id.equals(id))
                    .collect(Collectors.toList())
                    .get(0);
        }
        catch (Exception e)
        {
            System.out.println("findById" + this.getClass());
            course = null;
        }
        return course;
    }
}
