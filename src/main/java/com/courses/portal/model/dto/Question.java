package com.courses.portal.model.dto;

import com.courses.portal.model.dto.Image;
import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * Created by jonathan on 7/15/17.
 */
public class Question {
    @Expose
    public String statement;
    @Expose
    public Map<String,String> alternatves;
    @Expose
    public Character correct;
    @Expose
    public Image image;
    @Expose
    public Boolean status;
}
