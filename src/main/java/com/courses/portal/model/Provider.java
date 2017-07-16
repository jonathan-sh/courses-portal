package com.courses.portal.model;

import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.security.Encryption;
import com.courses.portal.useful.constants.CauseDescription;
import com.courses.portal.useful.mongo.MongoHelper;
import com.courses.portal.useful.constants.DetailsDescription;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sound.midi.Soundbank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by jonathan on 7/11/17.
 */
public class Provider{

    public Provider() {
        this.validation.status = false;
        this.validation.HttpStatus = HttpStatus.MULTI_STATUS;
    }

    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String email;
    @Expose
    public String password;
    @Expose
    public String description;
    @Expose
    public String header;
    @Expose
    public List<Image> images;
    @Expose
    public Boolean status;
    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();

    public String BCryptEncoderPassword() {
        return new BCryptPasswordEncoder().encode(this.password);
    }

    public Provider fieldValidationForCreation() {
        this.validation.status =   this.name != null &&
                                   this.email != null &&
                                   this.password != null &&
                                   !this.name.isEmpty() &&
                                   !this.email.isEmpty() &&
                                   !this.password.isEmpty();

        if (!this.validation.status)
        {
            this.validation.cause = CauseDescription.FIELDS_ERROR.get();
            this.validation.details = DetailsDescription.FIELDS_REQUIRED.get() + requirementsForCreation();
            this.validation.HttpStatus = HttpStatus.NOT_ACCEPTABLE;
        }


        return this;

    }

    private String  requirementsForCreation()
    {
        return "< name, email, password >";
    }

    public Provider treatmentForCreate(){
        if(validation.status){
            this.password = Encryption.generateHash(this.password);
            this.email = this.email.toLowerCase();
            this.status = true;
            this._id = null;
        }
        return this;
    }

    public Provider fieldValidationUpdate() {
        boolean premise =   this._id != null ;

        if (!premise)
        {
            this.validation.status = premise;
            this.validation.cause = CauseDescription.FIELDS_ERROR.get();
            this.validation.details = DetailsDescription.FIELDS_REQUIRED.get() + requirementsForUpdate();
        }
        this.validation.status = premise;

        return this;

    }

    private String  requirementsForUpdate()
    {
        return "< _id >";
    }

    public Provider treatmentForUpdate(){
        this.email = null;
        if ( this.password != null) {this.password = Encryption.generateHash(this.password);}
        return this;
    }

    public Provider validationOfExistence(){

        if (validation.status)
        {
            validation.status = providerRepository.findByEmail(this.email) == null;

            if (!validation.status)
            {
                validation.cause = CauseDescription.ALREADY_EXISTS.get();
                validation.details = DetailsDescription.ALREADY_EXISTS.get() + this.email;
                this.validation.HttpStatus = HttpStatus.NOT_ACCEPTABLE;
            }
        }

        return this;
    }

    public Provider treatmentForResponse(){
        if(this._id!=null){this._id = MongoHelper.treatsId(this._id);}
        if(this.password!=null){this.password = DetailsDescription.PASSWORD.get();}
        return this;
    }


    @Expose(serialize = false)
    public static final String COLLECTION ="provider";
    @Expose(serialize = false)
    private ProviderRepository providerRepository = new ProviderRepository(COLLECTION,this.getClass());
    public Provider create(){
        boolean wasCreated = false;
        if (validation.status)
        {
            wasCreated = providerRepository.create(this);
            if (wasCreated)
            {
                this._id = providerRepository.findByEmail(this.email)._id;
                this.validation.HttpStatus = HttpStatus.CREATED;
            }
        }

        return  this;
    }

    public Provider update(){
        boolean wasUpdated = false;
        if (validation.status)
        {
            wasUpdated = providerRepository.update(this._id,this);
            if (wasUpdated)
            {

               try
               {
                   Provider result = (Provider) providerRepository.readOne(this._id);
                   result.validation.HttpStatus = HttpStatus.OK;
                   result.validation.status = wasUpdated;
                   return result;
               }
               catch (Exception e)
               {

               }

            }
        }

        return  this;
    }

}
