package com.courses.portal.controller;

import com.courses.portal.model.HomeInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonathan on 7/15/17.
 */
@RestController
@RequestMapping(value = "/home")
public class HomeCtrl {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> homeInformation() {

        return new ResponseEntity<>(new HomeInformation().get(), HttpStatus.OK);

    }

}
