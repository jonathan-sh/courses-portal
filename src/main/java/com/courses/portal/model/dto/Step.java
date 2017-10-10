package com.courses.portal.model.dto;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.model.Course;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jonathan on 7/15/17.
 */
public class Step {
    private static Logger logger = LoggerFactory.getLogger(Step.class);
    @Expose
    public Object _id;
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
    private CourseRepository courseRepository = new CourseRepository(Course.COLLECTION, Course.class);

    Integer count = 0;
    public List<Step> validEndPlusOrder(String id, List<Step> listSteps) {
        Course course = courseRepository.findById(id);
        if (listSteps != null && course != null && course.steps != null)
        {
            count = course.steps.size();
        }

        listSteps.forEach(item -> {
            if (item.order == null)
            {
                count++;
                item.order = count;
            }

            try
            {
                Boolean contains = course.steps.contains(item);
                if (contains)
                {
                    course.steps.remove(item);
                }
            }
            catch (Exception e)
            {
               logger.error(String.valueOf(e.getStackTrace()));
            }
        });

        try
        {
            course.steps.forEach(item->{
                if (item!=null)
                {
                    listSteps.add(item);
                }
            });
        }
        catch (Exception e)
        {
            logger.error(String.valueOf(e.getStackTrace()));
        }


        return listSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Step step = (Step) o;

        return order.equals(step.order);
    }

    @Override
    public int hashCode() {
        return order.hashCode();
    }
}
