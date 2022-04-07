package com.project.contacts.contacts.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("spring.mail.username")
    String sender;

    public boolean sendEmail(String subject, String to, String url) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            String newBody = new String();
            newBody = newBody
                    + "VERIFICATION REQUEST\n Welcome To Contact Manager Application \n Please click this url to verify\n"
                    + url;

            msg.setTo(to);
            msg.setSubject(subject);

            msg.setText(newBody);

            msg.setFrom(sender);
            javaMailSender.send(msg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}