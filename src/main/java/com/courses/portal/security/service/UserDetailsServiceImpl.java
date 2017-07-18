package com.courses.portal.security.service;


import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.dao.StudentRepository;
import com.courses.portal.model.Provider;
import com.courses.portal.model.Student;
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


    private static ProviderRepository providerRepository = new ProviderRepository(Provider.COLLECTION, Provider.class);
    private static StudentRepository studentRepository = new StudentRepository(Student.COLLECTION, Student.class);
    private String email;
    private String entity;
    private SpringSecurityUser getSpringSecurityUser() {

        SpringSecurityUser springSecurityUser = null;
        switch (entity)
        {
            case "provider":
                Provider provider = this.providerRepository.findByEmail(email);
                if (provider != null && provider.isValid())
                {
                    springSecurityUser = new SpringSecurityUser(provider._id,
                                         provider.email,
                                         provider.BCryptEncoderPassword(),
                                         provider.name,
                                         null,
                                         AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));

                }

                break;
            case "student":
                Student student = this.studentRepository.findByEmail(email);
                if (student!=null && student.isValid())
                {
                    springSecurityUser = new SpringSecurityUser(student._id,
                                         student.email,
                                         student.BCryptEncoderPassword(),
                                         student.name,
                                         null,
                                         AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));
                }

                break;
        }

        return springSecurityUser;
    }

    public static String REGEX = ":::";

    private void splitEndValidationOfEmailEntity(String emailEndEntity) {
        String[] emailEntity = emailEndEntity.split(this.REGEX);
        this.isValidRequest = emailEntity.length == 2;
        if (this.isValidRequest)
        {
            this.email = emailEntity[0];
            this.entity = emailEntity[1];
        }

    }
}
