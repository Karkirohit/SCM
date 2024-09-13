package com.scm.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body) {
       
    SimpleMailMessage simpleMailMessage=new SimpleMailMessage();

    simpleMailMessage.setTo(to);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(body);
    simpleMailMessage.setFrom(domainName);

    javaMailSender.send(simpleMailMessage);

    }

    @Override
    public void sendEmailWithHtml() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithHtml'");
    }

    @Override
    public void sendEmailWithAttachment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithAttachment'");
    }

}
