package com.courses.portal.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 7/15/17.
 */
public class Question {
    @Expose
    public String header;
    @Expose
    public String [] alternatves;
    @Expose
    public Integer correct;
    @Expose
    public Image image;
    @Expose
    public Boolean status;

    public  Boolean fieldValidationForCreation(){
        return this.header != null &&
               this.alternatves != null &&
               this.alternatves.length ==5 &&
               this.correct != null &&
               this.status != null ;

    }

}
