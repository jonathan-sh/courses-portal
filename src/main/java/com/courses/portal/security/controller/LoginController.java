package com.courses.portal.security.controller;


import com.courses.portal.model.dto.Response;
import com.courses.portal.security.TokenUtils;
import com.courses.portal.security.model.Login;
import com.courses.portal.useful.constants.DetailsDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
public class LoginController {


    private AuthenticationManager authenticationManager;
    private TokenUtils tokenUtils;
    private UserDetailsService userDetailsService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

   @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody Login login)
            throws AuthenticationException {

        Authentication authentication = getAuthenticate(login);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(getResponse(login));
    }

    private Authentication getAuthenticate(Login login) {
        System.out.println(login.getUserNameSpring());
        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUserNameSpring(), login.getPassword()));
    }

    private Response getResponse(Login login) {
        Response response = new Response();
        if (login.validation.status != null && login.validation.status)
        {
            response.token = DetailsDescription.PASSWORD.get();
            response.entity = login.validation;
            return response;
        }
        response.token = this.tokenUtils.generateToken(login);
        response.entity = login.findEntity();
        return response;
    }
}
