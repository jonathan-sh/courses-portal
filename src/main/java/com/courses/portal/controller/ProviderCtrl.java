package com.courses.portal.controller;

import com.courses.portal.model.Provider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonathan on 7/11/17.
 */

@RestController
@RequestMapping(value = "/provider")
public class ProviderCtrl {


    private Provider provider;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody Provider provider) {
        this.provider = provider.fieldValidationForCreation()
                                .treatmentForCreate()
                                .validationOfExistence()
                                .create()
                                .treatmentForResponse();
        return makeResponse();

    }

    private ResponseEntity makeResponse() {
        if (this.provider.validation.status) {
            return new ResponseEntity(this.provider, this.provider.validation.HttpStatus);
        } else {
            return new ResponseEntity(this.provider.validation, this.provider.validation.HttpStatus);
        }
    }


}


