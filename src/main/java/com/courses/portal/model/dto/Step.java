package com.courses.portal.model.dto;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.model.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jonathan on 7/15/17.
 */
public class Step {
    private static Logger logger = LoggerFactory.getLogger(Step.class);
    @Expose
    public String _id;
    @Expose
    public String courseId;
    @Expose
    public Integer order;
    @Expose
    public String name;
    @Expose
    public String description;
    @Expose
    public List<Material> materials;
    @Expose
    public List<Question> questions;
    @Expose
    public List<Exam> exams;
    @Expose
    public Integer chances;
    @Expose(serialize = false)
    public Validation validation;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Step step = (Step) o;

        return _id.equals(step._id);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    @Expose(serialize = false)
    private CourseRepository courseRepository = new CourseRepository(Course.COLLECTION, Course.class);


    @JsonIgnore
    public Course create() {
        Course course = courseRepository.findById(this.courseId);
        if (course!=null)
        {
            this.order = getListOrder(course);
            this._id = new ObjectId().toString();
            course.steps = (course.steps==null)? new ArrayList<>() : course.steps;
            course.steps.add(this);
        }
        return course.updateRotine();
    }

    @JsonIgnore
    public Course update() {

        Course course = courseRepository.findById(this.courseId);
        if (course!=null)
        {
            try
            {
                Step stepToUpdate = course.steps
                                          .stream()
                                          .filter(step -> step.equals(this))
                                          .collect(Collectors.toList())
                                          .get(0);

                course.steps.remove(stepToUpdate);
                course.steps.add(this);

            }
            catch (Exception e)
            {
                System.out.println("não deletou etapa");
            }
        }

        return course.updateRotine();
    }

    @JsonIgnore
    public Course delete() {

        Course course = courseRepository.findById(this.courseId);
        if (course!=null)
        {
            try
            {
                Step stepToDelete = course.steps
                                          .stream()
                                          .filter(step -> step.equals(this))
                                          .collect(Collectors.toList())
                                          .get(0);

                course.steps.remove(stepToDelete);

            }
            catch (Exception e)
            {
                System.out.println("não deletou etapa");
            }
        }

        return course.updateRotine();
    }

    public Integer getOrder() {
        return order;
    }

    @JsonIgnore
    private Integer getListOrder(Course course) {
        try
        {
            Integer count =  course.steps
                                   .stream()
                                   .sorted(Comparator.comparing(Step::getOrder).reversed())
                                   .collect(Collectors.toList())
                                   .get(0).order;
            return count + 1;
        }
        catch (Exception e)
        {
            return 1;
        }
    }
}
