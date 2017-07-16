package com.courses.portal.model;

import com.google.gson.annotations.Expose;

/**
 * Created by jonathan on 7/15/17.
 */
public class Material {
    @Expose
    public Integer order;
    @Expose
    public String name;
    @Expose
    public String type;
    @Expose
    public String url;
    @Expose
    public Boolean status;

    public Boolean fieldValidationForCreation()
    {
        return this.order != null &&
               this.name != null &&
               this.type != null &&
               this.url != null &&
               this.status != null;
    }




}
