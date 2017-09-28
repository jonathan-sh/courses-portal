package com.courses.portal.security.model;

import com.courses.portal.dao.CourseRepository;
import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.dao.StudentRepository;
import com.courses.portal.model.*;
import com.courses.portal.model.dto.Response;
import com.courses.portal.model.dto.Validation;
import com.courses.portal.security.constants.AppConstant;
import com.courses.portal.security.constants.Entity;
import com.courses.portal.useful.constants.DetailsDescription;
import com.courses.portal.useful.encryptions.EncryptionSHA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class Login {
    private static Logger logger = LoggerFactory.getLogger(Login.class);

    private String oldPassword;
    private String password;
    private String newPassword;
    private String email;
    private String entity;
    public Validation validation = new Validation();
    public String generatedPassword;
    public String _id;
    public String url;


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

    private String requirements() {
        return "< email, entity >";
    }

    private String requirementsForResetPassword() {
        return "< id, oldPassword, newPassword, entity >";
    }

    private static ProviderRepository providerRepository = new ProviderRepository(Provider.COLLECTION, Provider.class);
    private static StudentRepository studentRepository = new StudentRepository(Student.COLLECTION, Student.class);

    public Login makeForgotPassword() {
        this.generatedPassword = EncryptionSHA.generateHash(LocalDateTime.now().toString());
        if (validation.status)
        {
            switch (entity)
            {
                case Entity.PROVIDER:
                    Provider provider = providerRepository.findByEmail(email);
                    if (provider != null)
                    {
                        provider.password = generatedPassword;
                        provider.validation.status = true;
                        provider.treatmentForUpdate()
                                .update();
                        this._id = provider._id.toString();
                    }
                    else
                    {
                        makeNotFound();
                    }

                    break;
                case Entity.STUDENT:
                    Student student = studentRepository.findByEmail(email);
                    if (student != null)
                    {
                        student.password = generatedPassword;
                        student.validation.status = true;
                        student.treatmentForUpdate()
                                .update();
                        this._id = student._id.toString();
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

    private void makeNotFound() {
        validation.notFound(email);
    }

    public Login genereteUrlForResetUpdade() {
        if (validAfterMakeUrl())
        {
            this.url = this.entity + "/" + this._id + "/" + this.generatedPassword;
        }
        return this;
    }

    private boolean validAfterMakeUrl() {
        return this._id != null &&
                this.generatedPassword != null &&
                !this._id.isEmpty() &&
                !this.generatedPassword.isEmpty();
    }

    public Login validationForPasswordUpdade(String id, String password) {
        this._id = id;
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

    public Object getEntity() {
        switch (entity)
        {
            case Entity.PROVIDER:

                 Provider provider = (Provider) providerRepository.readOne(this._id);
                 provider.treatmentForResponse();
                 Response.ProviderCourse providerCourse = new Response() .new ProviderCourse();
                 providerCourse.provider = provider;
                 providerCourse.courses = new CourseRepository(Course.COLLECTION, Course.class).findAll();
                 return providerCourse;

            case Entity.STUDENT:
                 Student student = (Student) studentRepository.readOne(this._id);
                 student.treatmentForResponse();
                 return student;

            default:
                validation.noContains(DetailsDescription.NOT_CONTAINS_ENTITY.get());
                return this.validation;

        }

    }


}

