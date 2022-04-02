package com.project.contacts.contacts.Utilities;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {
    private String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
            + "lmnopqrstuvwxyz";
    private Random rnd = new Random();

    public String generateUserId(int len) {
        return generateRandomString(len);
    }

    private String generateRandomString(int len) {

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}