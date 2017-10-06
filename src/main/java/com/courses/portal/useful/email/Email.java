package com.courses.portal.useful.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import java.net.MalformedURLException;

/**
 * Created by jonathan on 4/5/17.
 */
public class Email {

    private static String HostName = "smtp.googlemail.com";
    private static Integer SmtpPort = 587;
    private static String EmailRemetente = "thedionisio.birl@gmail.com";
    private static String Senha = "qnvaidaroq";

    public static void sendSimpleEmail(String Destinatario, String Assunto, String TextoSimples) throws EmailException{
        SimpleEmail email = new SimpleEmail();
        email.setHostName(HostName);
        email.setSmtpPort(SmtpPort);
        email.setAuthenticator(new DefaultAuthenticator(EmailRemetente, Senha));
        email.setSSLOnConnect(true);
        email.setFrom(EmailRemetente);
        email.addTo(Destinatario);
        email.setSubject(Assunto);
        email.setMsg(TextoSimples);
        email.send();

        System.out.println("Email de texto simples enviado para :" + Destinatario);
    }


    public static void sendHtmlEmail(String Destinatario, String Assunto, String TextoHtml, String TextoSimples) throws EmailException, MalformedURLException{

        HtmlEmail email = new HtmlEmail();
        email.setHostName(HostName);
        email.setSmtpPort(SmtpPort);
        email.setAuthenticator(new DefaultAuthenticator(EmailRemetente, Senha));
        email.setSSLOnConnect(true);
        email.setFrom(EmailRemetente);
        email.addTo(Destinatario);
        email.setSubject(Assunto);
        email.setHtmlMsg(TextoHtml);
        email.setTextMsg(TextoSimples);
        email.send();

        System.out.println("Email de texto html enviado para :" + Destinatario);
    }


}