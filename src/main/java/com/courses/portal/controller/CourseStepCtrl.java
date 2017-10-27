package com.courses.portal.controller;

import com.courses.portal.model.Course;
import com.courses.portal.model.dto.Step;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonathan on 7/15/17.
 */
@RestController
@RequestMapping(value = "/course-step")
public class CourseStepCtrl {


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Step step) {

        return makeResponse(step.create());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Step step) {

        return makeResponse(step.update());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@RequestBody Step step) {

        return makeResponse(step.delete());

    }

    private ResponseEntity<Object> makeResponse(Course course) {
        if (course.validation.status)
        {
            return new ResponseEntity<>(course, course.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(course.validation, course.validation.httpStatus);
        }
    }

}
