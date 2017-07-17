package com.courses.portal.model;

import com.courses.portal.dao.StudentRepository;
import com.courses.portal.security.Encryption;
import com.courses.portal.useful.constants.DetailsDescription;
import com.courses.portal.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by jonathan on 7/16/17.
 */
public class Student {

    private static Logger logger = LoggerFactory.getLogger(Student.class);

    @Expose
    public Object _id;
    @Expose
    public String name;
    @Expose
    public String email;
    @Expose
    public String password;
    @Expose
    public String urlImage;
    @Expose
    public String zipCode;
    @Expose
    public String street;
    @Expose
    public String city;
    @Expose
    public String number;
    @Expose
    public Boolean status;
    @JsonIgnore
    @Expose(serialize = false)
    public Validation validation = new Validation();

    public String BCryptEncoderPassword() {
        return new BCryptPasswordEncoder().encode(this.password);
    }

    public Student fieldValidationForCreation() {
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

    public Student treatmentForCreate() {
        if (validation.status)
        {
            this.email = this.email.toLowerCase();
            this.password = Encryption.generateHash(this.password);
            this.status = true;
            this._id = null;
        }
        return this;
    }

    public Student fieldValidationUpdate() {
        boolean premise = this._id != null;

        if (!premise)
        {
            this.validation.fieldsError(requirementsForUpdate());
        }

        this.validation.status = premise;

        return this;

    }

    private String requirementsForUpdate() {
        return "< _id >";
    }

    public Student treatmentForUpdate() {
        this.email = null;
        if (this.password != null)
        {
            this.password = Encryption.generateHash(this.password);
        }
        return this;
    }

    public Student validationOfExistence() {

        if (validation.status)
        {
            validation.status = studentRepository.findByEmail(this.email) == null;
            if (!validation.status)
            {
                validation.alreadyExists(this.email);
            }
        }

        return this;
    }

    public Student treatmentForResponse() {
        if (this._id != null)
        {
            this._id = MongoHelper.treatsId(this._id);
        }
        if (this.password != null)
        {
            this.password = DetailsDescription.PASSWORD.get();
        }
        return this;
    }


    @Expose(serialize = false)
    public static final String COLLECTION = "provider";
    @Expose(serialize = false)
    private StudentRepository studentRepository = new StudentRepository(COLLECTION, this.getClass());

    public Student create() {
        boolean wasCreated = false;
        if (validation.status)
        {
            wasCreated = studentRepository.create(this);
            if (wasCreated)
            {
                this._id = studentRepository.findByEmail(this.email)._id;
                this.validation.HttpStatus = HttpStatus.CREATED;
            }
        }

        return this;
    }

    public Student update() {
        boolean wasUpdated = false;
        if (validation.status)
        {
            wasUpdated = studentRepository.update(this._id, this);
            if (wasUpdated)
            {

                try
                {
                    Student result = (Student) studentRepository.readOne(this._id);
                    result.validation.HttpStatus = HttpStatus.OK;
                    result.validation.status = wasUpdated;
                    return result;
                }
                catch (Exception e)
                {

                    logger.error("Error during cast to Student");
                    logger.error("Possible cause: " + e.getCause());
                }

            }
        }

        return this;
    }

}

