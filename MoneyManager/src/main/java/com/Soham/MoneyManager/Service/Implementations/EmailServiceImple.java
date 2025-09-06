package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImple implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
    @Override
    public void sendEmail(String to, String sub, String body) {
        try{
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(sub);
            mailMessage.setText(body);
            javaMailSender.send(mailMessage);


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
