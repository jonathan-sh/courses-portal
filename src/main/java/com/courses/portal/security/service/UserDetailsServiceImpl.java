package com.courses.portal.security.service;


import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.model.dto.Provider;
import com.courses.portal.security.model.SpringSecurityUser;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Provider provider = null;
        try
        {
            provider = (Provider) new ProviderRepository().findByEmail(email);

        }
        catch (Exception e)
        {
            System.out.println("log de erro de pessoa");
        }

        if (provider != null)
        {
            SpringSecurityUser springSecurityUser = new SpringSecurityUser(
                    provider._id,
                    provider.email,
                    provider.BCryptEncoderPassword(),
                    provider.name,
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN"));

            return springSecurityUser;
        }
        else
        {
            throw new UsernameNotFoundException(String.format("No appUser found with email '%s'.", email));
        }
    }

}
