package com.courses.portal.controller;

import com.courses.portal.model.Provider;
import com.courses.portal.security.constants.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jonathan on 7/11/17.
 */

@RestController
@RequestMapping(value = "/provider")
public class ProviderCtrl {


    private Provider provider;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> find(@RequestHeader(AppConstant.TOKEN_HEADER) String header) {
        this.provider = new Provider().get(header);
        return makeResponse();

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Provider provider) {
        this.provider = provider.fieldValidationForCreation()
                                .treatmentForCreate()
                                .validationOfExistence()
                                .create()
                                .treatmentForResponse();
        return makeResponse();

    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Provider provider) {
        this.provider = provider.fieldValidationUpdate()
                                .treatmentForUpdate()
                                .update()
                                .treatmentForResponse();
        return makeResponse();

    }

    private ResponseEntity<Object> makeResponse() {
        if (this.provider.validation.status)
        {
            return new ResponseEntity<>(this.provider, this.provider.validation.httpStatus);
        }
        else
        {
            return new ResponseEntity<>(this.provider.validation, this.provider.validation.httpStatus);
        }
    }


}


