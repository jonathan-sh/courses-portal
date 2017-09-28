package com.courses.portal.controller;

import com.courses.portal.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonathan on 7/17/17.
 */

@RestController
@RequestMapping(value = "/student")
public class StudentCtrl {

    private Student student = new Student();

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Student student) {
        this.student = student.fieldValidationForCreation()
                              .treatmentForCreate()
                              .validationOfExistence()
                              .create()
                              .treatmentForResponse();
        return makeResponse();

    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Student student) {
        this.student = student.fieldValidationUpdate()
                              .update()
                              .treatmentForResponse();
        return makeResponse();

    }

    private ResponseEntity<Object> makeResponse() {
        if (this.student.validation.status)
        {
            return new ResponseEntity<>(this.student, this.student.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(this.student.validation, this.student.validation.httpStatus);
        }
    }

}
