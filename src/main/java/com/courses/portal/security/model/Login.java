package com.courses.portal.security.model;

import com.courses.portal.security.Encryption;
import com.courses.portal.security.constants.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login {
    private static Logger logger = LoggerFactory.getLogger(Login.class);
    private String userNameSpring;
    private String email;
    private String password;
    private String entity;


    public Login() {
        super();
    }

    public Login(String email, String password, String entity) {
        this.setEmail(email);
        this.setPassword(password);
        this.setEntity(entity);
    }

    public String getUserNameSpring() {
        if (!isValid())
        {
            return null;
        }
        return this.email + AppConstant.REGEX + this.entity;
    }

    public void setUserNameSpring(String userNameSpring) {
        this.userNameSpring = userNameSpring;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = Encryption.generateHash(password);
    }

    public String getEntity() {
       return this.entity;
    }

    private void setEntity(String entity) {
        this.entity = entity;
    }

    private boolean isValid(){
       return this.email !=null &&
              this.password != null &&
              this.entity != null &&
              !this.email.isEmpty() &&
              !this.password.isEmpty() &&
              !this.entity.isEmpty();
    }
}
