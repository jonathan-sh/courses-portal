package com.courses.portal.controller;

import com.courses.portal.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "/information/{id}",method = RequestMethod.GET)
    public ResponseEntity<Object> findOne(@PathVariable String id) {
        this.course = course.getInformationForHome(id);
        return makeResponse();
    }

    @RequestMapping(path = "/analytical",method = RequestMethod.GET)
    public ResponseEntity<Object> analytical() {
        return new ResponseEntity<>(this.course.analytics(), HttpStatus.OK);
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

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@RequestBody Course course) {

        return new ResponseEntity<>(course.delete(), HttpStatus.OK);

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
