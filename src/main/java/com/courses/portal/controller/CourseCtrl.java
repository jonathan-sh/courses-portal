package com.courses.portal.controller;

import com.courses.portal.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonathan on 7/15/17.
 */
@RestController
@RequestMapping(value = "/course")
public class CourseCtrl {

    private Course course = new Course();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> readAll() {
        return new ResponseEntity<>(course.readAll(), HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Course course) {
        this.course = course.fieldValidationForCreation()
                            .treatmentForCreate()
                            .create()
                            .treatmentForResponse();
        return makeResponse();

    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Course course) {
        this.course = course.fieldValidationUpdate()
                            .update()
                            .treatmentForResponse();
        return makeResponse();

    }

    private ResponseEntity<Object> makeResponse() {
        if (this.course.validation.status)
        {
            return new ResponseEntity<>(this.course, this.course.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(this.course.validation, this.course.validation.httpStatus);
        }
    }

}
