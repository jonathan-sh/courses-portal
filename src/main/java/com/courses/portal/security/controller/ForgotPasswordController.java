package com.courses.portal.security.controller;

import com.courses.portal.security.model.Login;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jonathan on 7/24/17.
 */
@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private Login login;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> forgot(@RequestBody Login login){

        this.login = login.validForForgotPassword()
                          .makeForgotPassword()
                          .genereteUrlForResetUpdade();

        return makeResponse();
    }

    private ResponseEntity<Object> makeResponse() {
        if (this.login.validation.status)
        {
            return new ResponseEntity<>(this.login, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(this.login.validation, this.login.validation.httpStatus);
        }
    }

    @RequestMapping(path = "/{email}/{password}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Login login,
                                    @PathVariable String email,
                                    @PathVariable String password)
    {
        login.validationForPasswordUpdade(email,password)
             .makePasswordUpdade();
        return makeResponse();
    }


}
