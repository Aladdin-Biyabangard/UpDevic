package com.team.updevic001.mail;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ConfirmationEmailServiceImpl {

    private final JavaMailSender mailSender;

    public void sendEmail(String receiver, EmailTemplate template, Map<String, String> placeholders) {
        try {
            log.info("Operation of sending email started to {}", receiver);

            SimpleMailMessage message = getMessage(receiver, template, placeholders);
            mailSender.send(message);
            log.info("Email message successfully sent to {}", receiver);

        } catch (Exception e) {
            log.error("Exception occurs while sending email: Description: {}", e.getMessage(), e);
        }
    }

    private static SimpleMailMessage getMessage(String receiver, EmailTemplate template, Map<String, String> placeholders) {
        String subject = template.getSubject();
        String body = template.getBody();

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            body = body.replace(placeholder, entry.getValue());
            subject = subject.replace(placeholder, entry.getValue());
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);
        return message;
    }
}