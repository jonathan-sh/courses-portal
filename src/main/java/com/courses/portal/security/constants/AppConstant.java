package com.courses.portal.security.constants;

import com.courses.portal.dao.ProviderRepository;
import com.courses.portal.model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppConstant {
    public static final String TOKEN_HEADER = "X-Auth-Token";
    public static final String REGEX = ":::";
    public static String SE_EMAIL;
    public static String SE_PASSWORD;
    public static String SE_PORT;
    public static String SE_HOSTNAME;

    @Autowired
    public AppConstant() {
       try
       {
           Provider provider =
                   new ProviderRepository(Provider.COLLECTION,Provider.class).getOne();

           if (provider!=null || provider.configEmail != null)
           {
               AppConstant.SE_EMAIL = provider.configEmail.email;
               AppConstant.SE_PASSWORD = provider.configEmail.password;
               AppConstant.SE_PORT = provider.configEmail.port;
               AppConstant.SE_HOSTNAME = provider.configEmail.hostname;

           }
       }
       catch (Exception e)
       {
           System.out.println("Não há email configurado.");
       }
    }
}
