package com.courses.portal.model;

import com.courses.portal.model.dto.Validation;
import com.courses.portal.useful.email.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.apache.commons.mail.EmailException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class SendEmail {
    public String subject;
    public String text;
    public String html;
    public List<String> recipients = new ArrayList<>();

    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();


    @JsonIgnore
    public SendEmail fieldValidationForSend() {
        this.validation.status = this.subject != null &&
                                 this.text != null &&
                                 this.recipients.size() != 0;

        if (!this.validation.status)
        {
            this.validation.fieldsError(requirementsForSend());
        }
        return this;
    }

    @JsonIgnore
    private String requirementsForSend() {
        return "< subject, text, recipients>";
    }

    @JsonIgnore
    public void send() {

        this.recipients.forEach(item ->{
            try
            {
                Email.sendHtmlEmail(item,this.subject,this.html,this.text);
            }
            catch (EmailException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        });

    }

}


