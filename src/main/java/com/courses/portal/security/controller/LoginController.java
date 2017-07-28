package com.courses.portal.security.controller;


import com.courses.portal.security.constants.AppConstant;
import com.courses.portal.security.TokenUtils;
import com.courses.portal.security.model.Login;
import com.courses.portal.security.model.SpringSecurityUser;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> mimimiTest(){
        return ResponseEntity.ok(true);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody Login login)
            throws AuthenticationException {

        Authentication authentication = getAuthenticate(login);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(getDocumentToken(login));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
        String token = request.getHeader(AppConstant.TOKEN_HEADER);
        String username = this.tokenUtils.getUsernameFromToken(token);
        SpringSecurityUser user = (SpringSecurityUser) getUserDetails(username);


        if (checkIfCanTokenBeRefreshed(token, user))
        {

            return ResponseEntity.ok(getDocumentRefreshToken(token));
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }


    }

    private Boolean checkIfCanTokenBeRefreshed(String token, SpringSecurityUser user) {
        return this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset());
    }


    private UserDetails getUserDetails(String userNameSpring) {
        return this.userDetailsService.loadUserByUsername(userNameSpring);
    }

    private Authentication getAuthenticate(Login login) {
        System.out.println(login.getUserNameSpring());
        return this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUserNameSpring(), login.getPassword()));
    }

    private Document getDocumentToken(Login login) {
        Document token = new Document();
        token.put("token", this.tokenUtils.generateToken(login));
        return token;
    }

    private Document getDocumentRefreshToken(String token) {
        Document refreshedToken = new Document();
        refreshedToken.put("token", this.tokenUtils.refreshToken(token));
        return refreshedToken;
    }

}
