package com.example.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendverficationEmail(String userEmail,
                                     String otp, String subject,String text) throws MailException, MailSendException
    {
       try
       {
           MimeMessage mimeMessage = javaMailSender.createMimeMessage();
           MimeMessageHelper mimeMessageHelper =  new MimeMessageHelper(
                   mimeMessage,"utf-8");

           mimeMessageHelper.setSubject(subject);
           mimeMessageHelper.setText(text);
           mimeMessageHelper.setTo(userEmail);

           javaMailSender.send(mimeMessage);
       }
       catch(MailException e)
       {
           System.out.println("error"+e);
           throw new MailSendException("failed to send email");
       }
       catch (MessagingException e) {
           throw new RuntimeException(e);
       }
    }
}
