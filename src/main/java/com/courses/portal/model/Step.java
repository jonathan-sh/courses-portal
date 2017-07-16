package com.courses.portal.model;

import java.util.List;

/**
 * Created by jonathan on 7/15/17.
 */
public class Step {
    public Object _id;
    public Integer order;
    public String name;
    public List<Material> materials;
    public List<Question> questions;
    public List<Exam> exams;
    public Integer chances;

    public Boolean fieldValidationForCreation() {

        return this.order != null &&
                this.name != null &&
                this.materials != null &&
                this.questions != null &&
                this.exams != null &&
                this.chances != null &&
                this.materials.size() > 0 &&
                this.questions.size() > 0 &&
                this.exams.size() > 0;

    }
}
