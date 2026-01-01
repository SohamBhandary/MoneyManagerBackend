package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImpleTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailServiceImple emailService;
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "from", "test@gmail.com");
    }

    @Test
    void sendEmail_success() {
        doNothing().when(javaMailSender).send(mimeMessage);

        assertDoesNotThrow(() ->
                emailService.sendEmail(
                        "user@gmail.com",
                        "Test Subject",
                        "<h1>Hello</h1>"
                )
        );

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void sendEmail_failure_shouldThrowRuntimeException() {
        doThrow(new RuntimeException("SMTP failure"))
                .when(javaMailSender)
                .send(mimeMessage);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> emailService.sendEmail(
                        "user@gmail.com",
                        "Test Subject",
                        "Body"
                )
        );

        assertTrue(ex.getMessage().contains("SMTP failure"));

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void sendEmailWithAttachment_success() throws MessagingException {
        doNothing().when(javaMailSender).send(mimeMessage);

        byte[] attachment = "dummy data".getBytes();

        assertDoesNotThrow(() ->
                emailService.sendEmailWithAttachment(
                        "user@gmail.com",
                        "Invoice",
                        "Please find attached",
                        attachment,
                        "invoice.pdf"
                )
        );

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
    }
}
