package com.courses.portal.security.controller;

import com.courses.portal.security.TokenUtils;
import com.courses.portal.security.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jonathan on 7/24/17.
 */
@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
    private TokenUtils tokenUtils;

    @Autowired
    public ForgotPasswordController(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> forgot(@RequestBody Login login){

       login.validForForgotPassword()
            .validIfItsExistence()
            .makeForgotPassword()
            .generateUrlForResetUpdate(this.tokenUtils)
            .treatsResponse();

        if (login.validation.status)
        {
            //send email
            return new ResponseEntity<>(login, HttpStatus.OK);
            //return new ResponseEntity<>("Email enviado", HttpStatus.OK);
        }

        return responseValidation(login);
    }


    @RequestMapping(path = "/token", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestHeader(value="Forgot-Password-Token") String token,
                                    @RequestBody Login login)
    {
        String tokenId = tokenUtils.getValueFronToken(token,"_id");
        String tokenPassword = tokenUtils.getValueFronToken(token,"generatedPassword");
        String tokenEntity = tokenUtils.getValueFronToken(token,"entity");

        login.validationForPasswordUpdate(tokenId,tokenPassword,tokenEntity)
             .makePasswordUpdate()
             .treatsResponse();

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
