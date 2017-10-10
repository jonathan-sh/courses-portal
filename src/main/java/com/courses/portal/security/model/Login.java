package com.courses.portal.security.model;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.dao.StudentRepository;
import com.courses.portal.model.Course;
import com.courses.portal.model.Provider;
import com.courses.portal.model.Student;
import com.courses.portal.model.dto.Response;
import com.courses.portal.model.dto.Validation;
import com.courses.portal.security.TokenUtils;
import com.courses.portal.security.constants.AppConstant;
import com.courses.portal.security.constants.Entity;
import com.courses.portal.useful.constants.DetailsDescription;
import com.courses.portal.useful.encryptions.EncryptionSHA;
import com.courses.portal.useful.mongo.MongoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class Login {
    /**
     * Attributes
     */
    private String oldPassword;
    private String password;
    private String newPassword;
    public String email;
    public String entity;
    @JsonIgnore
    public Validation validation = new Validation();
    @JsonIgnore
    public String generatedPassword;
    public String _id;
    public String url;


    /**
     * Basic methods
     */
    public Login() {
        super();
    }
    public Login(String email, String password, String entity) {
        this.setEmail(email);
        this.setPassword(password);
        this.setEntity(entity);
    }
    private void setEmail(String email) {
        this.email = email;
    }
    private void setPassword(String password) {
        this.newPassword = password;
        this.password = EncryptionSHA.generateHash(password);
    }
    private void setEntity(String entity) {
        this.entity = entity;
    }
    @JsonIgnore
    public String getUserNameSpring() {
        if (!isValid())
        {
            return null;
        }
        return this.email + AppConstant.REGEX + this.entity;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    private boolean isValid() {
        return this.email != null &&
               this.password != null &&
               this.entity != null &&
               !this.email.isEmpty() &&
               !this.password.isEmpty() &&
               !this.entity.isEmpty();
    }


    /**
     * Requirements
     * @return
     */

    private String requirements() {
        return "< email, entity >";
    }
    private String requirementsForResetPassword() {
        return "< id, oldPassword, newPassword, entity >";
    }

    /**
     * Data
     */

    private static ProviderRepository providerRepository = new ProviderRepository(Provider.COLLECTION, Provider.class);
    private static StudentRepository studentRepository = new StudentRepository(Student.COLLECTION, Student.class);
    private Provider provider;
    private Student student;


    /**
     * Making forgot password
     * @return
     */

    public Login validForForgotPassword() {
        this.validation.status = this.email != null &&
                this.entity != null &&
                !this.email.isEmpty() &&
                !this.entity.isEmpty();

        if (!validation.status)
        {
            validation.fieldsError(requirements());
        }
        return this;
    }

    public Login validIfItsExistence() {
        if (validation.status)
        {
            switch (entity)
            {
                case Entity.PROVIDER:
                    Provider provider = providerRepository.findByEmail(this.email);
                    if (provider != null )
                    {
                        this.provider = provider;
                    }
                    else
                    {
                        makeNotFound();
                    }
                    break;
                case Entity.STUDENT:
                    Student student = studentRepository.findByEmail(this.email);
                    if (student != null)
                    {
                        this.student = student;
                    }
                    else
                    {
                        makeNotFound();
                    }
                    break;
                default:
                    validation.noContains(DetailsDescription.NOT_CONTAINS_ENTITY.get());
                    break;
            }
        }
        return this;
    }

    public Login makeForgotPassword() {
        generatePassword();
        if (validation.status)
        {
            switch (entity)
            {
                case Entity.PROVIDER:
                        this.provider.password = generatedPassword;
                        this.provider.validation.status = true;
                        this.provider.treatmentForUpdate()
                                .update();
                        this._id = MongoHelper.treatsId(provider._id);
                        break;
                case Entity.STUDENT:
                        this.student.password = generatedPassword;
                        this.student.validation.status = true;
                        this.student.treatmentForUpdate()
                                .update();
                        this._id = student._id.toString();
                    break;
                default:
                    validation.noContains(DetailsDescription.NOT_CONTAINS_ENTITY.get());
                    break;
            }

        }
        return this;
    }

    public Login generateUrlForResetUpdate(TokenUtils tokenUtils) {
        if (validAfterMakeUrl())
        {
            this.url = tokenUtils.generateTokenForForgotPassword(this);
        }

        return this;
    }

    private boolean validAfterMakeUrl() {
        return this._id != null &&
                this.generatedPassword != null &&
                !this._id.isEmpty() &&
                !this.generatedPassword.isEmpty();
    }

    private void generatePassword() {
        this.generatedPassword = EncryptionSHA.generateHash(LocalDateTime.now().toString());
    }

    private void makeNotFound() {
        validation.notFound(email);
    }


    /**
     * Making update password
     */
    public Login validationForPasswordUpdate(String id, String password, String  entity) {
        this._id = id;
        this.entity = entity;
        this.oldPassword = password;
        this.validation.status = this.password != null &&
                this.entity != null &&
                this.oldPassword != null &&
                this._id != null &&
                !this.password.isEmpty() &&
                !this.entity.isEmpty() &&
                !this.oldPassword.isEmpty() &&
                !this._id.isEmpty();

        if (!this.validation.status)
        {
            validation.fieldsError(requirementsForResetPassword());
        }
        return this;
    }

    public Login makePasswordUpdate() {
        if (validation.status)
        {
            switch (entity)
            {
                case Entity.PROVIDER:
                    Provider provider = (Provider) providerRepository.readOne(this._id);
                    if (provider != null && validationOfOldPassword(provider.password))
                    {
                        provider.password = this.newPassword;
                        provider.validation.status = true;
                        provider.treatmentForUpdate()
                                .update();
                    }
                    else
                    {
                        makeNotContainsOldPasswordValid();
                    }
                    break;
                case Entity.STUDENT:
                    Student student = (Student) studentRepository.readOne(this._id);
                    if (student != null && validationOfOldPassword(student.password))
                    {
                        student.password = this.newPassword;
                        student.validation.status = true;
                        student.treatmentForUpdate()
                                .update();
                    }
                    else
                    {
                        makeNotContainsOldPasswordValid();
                    }
                    break;
                default:
                    validation.noContains(DetailsDescription.NOT_CONTAINS_ENTITY.get());
                    break;
            }
        }
        return this;
    }

    private void makeNotContainsOldPasswordValid() {
        validation.noContains(DetailsDescription.NOT_OLD_PASSWORD.get());
    }

    private boolean validationOfOldPassword(String password) {
        validation.status = EncryptionSHA.isPasswordValid(password, oldPassword);
        return validation.status;
    }


    /**
     * Global methods
     * @return
     */
    @JsonIgnore
    public Object findEntity() {
        switch (entity)
        {
            case Entity.PROVIDER:

                 Provider provider = providerRepository.findByEmail(this.email);
                 provider.treatmentForResponse();
                 Response.ProviderCourse providerCourse = new Response() .new ProviderCourse();
                 providerCourse.provider = provider;
                 providerCourse.courses = new CourseRepository(Course.COLLECTION, Course.class).findAll();
                 return providerCourse;

            case Entity.STUDENT:
                 Student student = studentRepository.findByEmail(this.email);
                 student.treatmentForResponse();
                 return student;

            default:
                validation.noContains(DetailsDescription.NOT_CONTAINS_ENTITY.get());
                return this.validation;

        }

    }

    @JsonIgnore
    public Login treatsResponse() {
        //this.password = DetailsDescription.PASSWORD.get();
        return this;
    }


}

