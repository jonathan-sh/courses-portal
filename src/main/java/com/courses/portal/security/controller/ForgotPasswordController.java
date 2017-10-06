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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> forgot(@RequestBody Login login){

       login.validForForgotPassword()
            .makeForgotPassword()
            .genereteUrlForResetUpdade();

        if (login.validation.status)
        {
            //send email
            return new ResponseEntity<>(login, HttpStatus.OK);
            //return new ResponseEntity<>("Email enviado", HttpStatus.OK);
        }

        return responseValidation(login);
    }


    @RequestMapping(path = "/{id}/{password}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Login login,
                                    @PathVariable String id,
                                    @PathVariable String password)
    {
        login.validationForPasswordUpdade(id,password)
             .makePasswordUpdate();

        if (login.validation.status)
        {
            return new ResponseEntity<>(login.validation.status, HttpStatus.OK);
        }

        return responseValidation(login);

    }

    private ResponseEntity<?> responseValidation(Login login) {
        return new ResponseEntity<>(login.validation,login.validation.httpStatus);
    }


}
