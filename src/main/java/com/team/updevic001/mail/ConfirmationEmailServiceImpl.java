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

//    public void sendEmail(String email, String subject, String token) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setFrom(fromEmail);
//            helper.setTo(email);
//            helper.setSubject(subject);
//            String linkText = buildEmailContentForVerify(token);
//            helper.setText(linkText, true);
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Email göndərmə zamanı xəta baş verdi!", e);
//        }
//    }
//
//    public String buildEmailContentForVerify(String token) {
//        String verificationLink = "http://localhost:8080/com.aladdin/university-site/api/auth/activate?token=" + token;
//        return """
//                <html>
//                    <body style="font-family: Arial, sans-serif; color: #333;">
//                        <h3 style="color: #007bff;">Qeydiyyat bildirişi!</h3>
//                        <p></p> <!-- Burada sənin göndərdiyin text olacaq -->
//                        <hr>
//                        <p>Qeydiyyatınızı təsdiqləmək üçün aşağıdakı linkə keçid edə bilərsiniz:</p>
//                        <p><a href="%s" style="color: #28a745; text-decoration: none;">Təsdiqləmə linkinə keç</a></p>
//                    </body>
//                </html>
//                """.formatted(verificationLink);
//    }
}