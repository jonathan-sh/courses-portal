package com.courses.portal.security.service;


import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.dao.StudentRepository;
import com.courses.portal.model.Provider;
import com.courses.portal.model.Student;
import com.courses.portal.security.constants.AppConstant;
import com.courses.portal.security.constants.Entity;
import com.courses.portal.security.model.Login;
import com.courses.portal.security.model.SpringSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
    private boolean isValidRequest;


    @Override
    public UserDetails loadUserByUsername(String emailEndEntity) throws UsernameNotFoundException {

        splitEndValidationOfEmailEntity(emailEndEntity);

        if (isValidRequest)
        {
            SpringSecurityUser springSecurityUser = getSpringSecurityUser();

            if (springSecurityUser != null)
            {
                return springSecurityUser;
            }
            throw new UsernameNotFoundException(String.format("No appUser found with email '%s'.", email));
        }
        else
        {
            throw new UsernameNotFoundException(String.format("No appUser found with email '%s'.", email));
        }
    }


    private static ProviderRepository providerRepository  = new ProviderRepository(Provider.COLLECTION, Provider.class);
    private static StudentRepository studentRepository = new StudentRepository(Student.COLLECTION, Student.class);
    private String email;
    private String entity;
    private SpringSecurityUser getSpringSecurityUser() {

        SpringSecurityUser springSecurityUser = null;
        switch (entity)
        {
            case Entity.PROVIDER:
                Provider provider = providerRepository.findByEmail(email);
                if (provider != null && provider.isValid())
                {
                    springSecurityUser = new SpringSecurityUser(provider._id,
                                         provider.email+AppConstant.REGEX+Entity.PROVIDER,
                                         provider.BCryptEncoderPassword(),
                                         provider.name,
                                         null,
                                         AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));

                }

                break;
            case Entity.STUDENT:
                Student student = studentRepository.findByEmail(email);
                if (student!=null && student.isValid())
                {
                    springSecurityUser = new SpringSecurityUser(student._id,
                                         student.email+AppConstant.REGEX+Entity.STUDENT,
                                         student.BCryptEncoderPassword(),
                                         student.name,
                                         null,
                                         AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));
                }

                break;
        }

        return springSecurityUser;
    }



    private void splitEndValidationOfEmailEntity(String emailEndEntity) {
        String[] emailEntity = emailEndEntity.split(AppConstant.REGEX);
        this.isValidRequest = emailEntity.length == 2;
        if (this.isValidRequest)
        {
            this.email = emailEntity[0];
            this.entity = emailEntity[1];
        }

    }
}
