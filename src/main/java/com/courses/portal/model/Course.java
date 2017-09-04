package com.courses.portal.model;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
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
    public Object id;
    @Expose
    public String name;
    @Expose
    public String description;
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
    @Expose
    public Integer views;
    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();


    public Course fieldValidationForCreation() {
        this.validation.status = this.name != null &&
                this.description != null &&
                this.objective != null &&
                this.hours != null &&
                this.price != null;

        if (!this.validation.status)
        {
            this.validation.fieldsError(requirementsForCreation());
        }

        return this;

    }

    private String requirementsForCreation() {
        return "< name, description, objective, hours, price, image, steps >";
    }

    public Course treatmentForCreate() {
        if (validation.status)
        {
            this._id = null;
        }
        return this;
    }

    public Course fieldValidationUpdate() {
        this.validation.status = true;
        boolean premise = this._id != null;
        if (this.steps != null)
        {
            steps = new Step().validEndPlusOrder(MongoHelper.treatsId(this._id), steps);
        }
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
            this.id = courseRepository.readAll().size() + 1;
            courseRepository.create(this);
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


    public Course getInformationForHome(String id) {
        Course course = courseRepository.findById(id);
        if (course!=null)
        {
            if(course.views!=null)
            {
                course.views++;
            }
            else
            {
                course.views =1;
            }

            courseRepository.update(course._id,course);

            if (!course.status)
            {
                course.validation = new Validation().notAcessible();
            }
            else
            {
                course.validation = new Validation().makeOK();
            }
        }
        else
        {
            this.validation = new Validation().notAcessible();
            return this;
        }

        return course;
    }

    public List<SerieHighchart> analitics(){
        SerieHighchart serieHighchart = new SerieHighchart();
        List<Course> allCouses = courseRepository.findAll();
        Double soma = allCouses.stream().mapToDouble(c->(c.views==null)?0:c.views).sum();
        allCouses.forEach(item->{
            DataHighchart data = new DataHighchart();
            data.name = item.name;
            data.y = new Double((item.views==null)?0:item.views) * 100 /soma;
            serieHighchart.data.add(data);
        });

        serieHighchart.name="Pei";
        List<SerieHighchart> list = new ArrayList<>();
        list.add(serieHighchart);
        return list;

    }
}
