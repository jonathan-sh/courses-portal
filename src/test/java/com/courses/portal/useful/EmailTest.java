package com.courses.portal.useful;

import com.courses.portal.useful.email.Email;
import org.apache.commons.mail.EmailException;
import org.junit.Test;

import java.net.MalformedURLException;

public class EmailTest {


    public void sendSimpleEmail() throws EmailException {
        new Email().sendSimpleEmail("jonathan.henrique.smtp@gmail.com",
                                       "Teste",
                                   "Teste de assunto");
    }


    public void sendHtmlEmail() throws EmailException, MalformedURLException {
        new Email().sendHtmlEmail("jonathan.henrique.smtp@gmail.com",
                "Teste",
                "<h1>Jonathan Henriuqe</h1>",
                "Teste de assunto");
    }

}
