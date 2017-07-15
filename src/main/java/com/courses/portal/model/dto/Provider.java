package com.courses.portal.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by jonathan on 7/11/17.
 */
public class Provider {

    public static final String COLLECTION ="provider";

    public Object _id;
    public String name;
    public String email;
    public String password;

    @Override
    public String toString() {
        return "Provider{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @JsonIgnore
    public String BCryptEncoderPassword() {
       return new BCryptPasswordEncoder().encode(this.password);
    }
}
