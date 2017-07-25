package com.courses.portal.model;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by jonathan on 7/15/17.
 */
public class Course {

    private static Logger logger = LoggerFactory.getLogger(Course.class);

    public Course() {
        this.validation.status = false;
        this.validation.httpStatus = HttpStatus.MULTI_STATUS;
    }

    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String operation;
    @Expose
    public String objective;
    @Expose
    public Integer hours;
    @Expose
    public Double price;
    @Expose
    public Image image;
    @Expose
    public List<Step> steps;
    @Expose
    public Boolean status;
    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();


    public Course fieldValidationForCreation() {
        this.validation.status = this.name != null &&
                                 this.operation != null &&
                                 this.objective != null &&
                                 this.hours != null &&
                                 this.price != null &&
                                 this.image != null &&
                                 this.steps != null;

        if (!this.validation.status)
        {
            this.validation.fieldsError(requirementsForCreation());
        }

        return this;

    }

    private String requirementsForCreation() {
        return "< name, operation, objective, hours, price, image, steps >";
    }

    public Course treatmentForCreate() {
        if (validation.status)
        {
            this.status = true;
            this._id = null;
        }
        return this;
    }

    public Course fieldValidationUpdate() {
        boolean premise = this._id != null;

        if (!premise)
        {
            this.validation.fieldsError(requirementsForUpdate());
        }

        return this;

    }

    private String requirementsForUpdate() {
        return "< _id >";
    }

    public Course treatmentForResponse() {
        if (this._id != null)
        {
            this._id = MongoHelper.treatsId(this._id);
        }
        return this;
    }


    @Expose(serialize = false)
    public static final String COLLECTION = "course";
    @Expose(serialize = false)
    private CourseRepository courseRepository = new CourseRepository(COLLECTION, this.getClass());

    public Course create() {
        if (validation.status)
        {
            this.validation.httpStatus = HttpStatus.CREATED;
        }
        return this;
    }

    public List<Course> readAll() {
        List<Course> courses = (List<Course>) courseRepository.readAll();
        courses.forEach(Course::treatmentForResponse);
        return courses;
    }

    public Course update() {
        boolean wasUpdated = false;
        if (validation.status)
        {
            wasUpdated = courseRepository.update(this._id, this);
            if (wasUpdated)
            {

                try
                {
                    Course result = (Course) courseRepository.readOne(this._id);
                    result.validation.httpStatus = HttpStatus.OK;
                    result.validation.status = wasUpdated;
                    return result;
                }
                catch (Exception e)
                {
                    logger.error("Error during cast to Course");
                    logger.error("Possible cause: " + e.getCause());
                }

            }
        }

        return this;
    }


}
