package com.courses.portal.model;

import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.model.dto.Grade;
import com.courses.portal.model.dto.Image;
import com.courses.portal.model.dto.ProviderTopic;
import com.courses.portal.model.dto.Validation;
import com.courses.portal.security.TokenUtils;
import com.courses.portal.security.constants.AppConstant;
import com.courses.portal.useful.encryptions.EncryptionSHA;
import com.courses.portal.useful.constants.DetailsDescription;
import com.courses.portal.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 7/11/17.
 */
public class Provider {

    public Provider() {
        this.validation.status = false;
        this.validation.httpStatus = HttpStatus.MULTI_STATUS;
    }

    private static Logger logger = LoggerFactory.getLogger(Provider.class);

    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String email;
    @Expose
    public String password;
    @Expose
    public String welcome;
    @Expose
    public List<Grade> grades = new ArrayList<Grade>();
    @Expose
    public List<ProviderTopic> topics = new ArrayList<ProviderTopic>();
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
        this.validation.status = this.name != null &&
                this.email != null &&
                this.password != null &&
                !this.name.isEmpty() &&
                !this.email.isEmpty() &&
                !this.password.isEmpty();

        if (!this.validation.status)
        {
            this.validation.fieldsError(requirementsForCreation());
        }

        return this;

    }

    private String requirementsForCreation() {
        return "< name, email, password >";
    }

    public Provider treatmentForCreate() {
        if (validation.status)
        {
            this.password = EncryptionSHA.generateHash(this.password);
            this.email = this.email.toLowerCase();
            this.status = true;
            this._id = null;
        }
        return this;
    }

    public Provider fieldValidationUpdate() {
        boolean premise = this._id != null;
        validation.status = true;
        if (!premise)
        {
            this.validation.fieldsError(requirementsForUpdate());
        }
        return this;
    }

    private String requirementsForUpdate() {
        return "< _id >";
    }

    public Provider treatmentForUpdate() {
        this.email = null;
        if (this.password != null)
        {
            this.password = EncryptionSHA.generateHash(this.password);
        }
        return this;
    }

    public Provider validationOfExistence() {
        if (validation.status)
        {
            validation.status = providerRepository.findByEmail(this.email) == null;
            if (!validation.status)
            {
                validation.alreadyExists(this.email);
            }
        }
        return this;
    }

    public Provider treatmentForResponse() {
        if (this._id != null)
        {
            this._id = MongoHelper.treatsId(this._id);
        }
        if (this.password != null)
        {
            this.password = DetailsDescription.PASSWORD.get();
        }
        if (this.grades == null)
        {
            this.grades = new ArrayList<>();
        }
        return this;
    }


    @Expose(serialize = false)
    public static final String COLLECTION = "provider";
    @Expose(serialize = false)
    private ProviderRepository providerRepository = new ProviderRepository(COLLECTION, this.getClass());

    public Provider create() {
        boolean wasCreated = false;
        if (validation.status)
        {
            wasCreated = providerRepository.create(this);
            if (wasCreated)
            {
                this._id = providerRepository.findByEmail(this.email)._id;
                this.validation.httpStatus = HttpStatus.CREATED;
            }
        }

        return this;
    }

    public Provider update() {
        this._id = MongoHelper.treatsId(this._id);
        boolean wasUpdated = false;
        if (validation.status)
        {
            wasUpdated = providerRepository.update(this._id, this);
            if (wasUpdated)
            {
                try
                {
                    Provider result = (Provider) providerRepository.readOne(this._id);
                    result.validation.httpStatus = HttpStatus.OK;
                    result.validation.status = wasUpdated;
                    return result;
                }
                catch (Exception e)
                {
                    logger.error("Error during cast to Provider");
                    logger.error("Possible cause: " + e.getCause());
                }

            }
        }

        return this;
    }

    public boolean isValid() {
        fieldValidationForCreation();
        return this.validation.status && this.status;
    }

    public Provider get(String header) {
        String email = new TokenUtils().getUsernameFromToken(header);
        String user[] = email.split(AppConstant.REGEX);
        Provider found = providerRepository.findByEmail(user[0]);
        found.validation = validation.makeOK();
        found.treatmentForResponse();
        return found;
    }
}