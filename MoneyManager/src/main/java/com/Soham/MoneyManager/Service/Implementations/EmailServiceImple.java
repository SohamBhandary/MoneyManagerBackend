package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImple implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(String to, String sub, String body) {
        log.info("Sending email to '{}' with subject '{}'", to, sub);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(sub);
            helper.setText(body, true); // <-- HTML content

            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to '{}': {}", to, e.getMessage(), e);

            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void sendEmailWithAttachment(String to ,String sub,String body, byte[] attch,String filename) throws MessagingException {
        log.info("Sending email with attachment '{}' to '{}' with subject '{}'", filename, to, sub);
        MimeMessage msg= javaMailSender.createMimeMessage();
        MimeMessageHelper help=new MimeMessageHelper(msg,true);
       help.setFrom(from);
       help.setTo(to);
       help.setSubject(sub);
       help.setText(body);
       help.addAttachment(filename,new ByteArrayResource(attch));
       javaMailSender.send(msg);

    }



}
