package com.Soham.MoneyManager.Service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(String to, String sub,String body);
    public void sendEmailWithAttachment(String to ,String sub,String body, byte[] attch,String filename) throws MessagingException;
}
