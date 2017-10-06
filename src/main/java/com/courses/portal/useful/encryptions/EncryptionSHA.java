package com.courses.portal.useful.encryptions;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

/**
 * Created by jonathan on 3/19/17.
 */
public class EncryptionSHA {

    private static Object salt;

    private static MessageDigestPasswordEncoder getInstanceMessageDisterPassword() {

        MessageDigestPasswordEncoder digestPasswordEncoder = new MessageDigestPasswordEncoder("SHA-1");

        return digestPasswordEncoder;

    }


    public static String generateHash(String word) {

        MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();

        String encodePassword = digestPasswordEncoder.encodePassword(word, salt);

        return encodePassword;

    }


    public static boolean isPasswordValid(String hashPassword, String password) {

        MessageDigestPasswordEncoder digestPasswordEncoder = getInstanceMessageDisterPassword();

        return digestPasswordEncoder.isPasswordValid(hashPassword, password, salt);

    }

}