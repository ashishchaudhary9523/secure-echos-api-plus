package com.devIntern.eslite.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.time.LocalDate;

@Service
public class EmailServiceImplementation {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${domain.url}")
    private String domain;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        String subject = "Verify Your Email - Secure Echos";

        // Verification link
        String verificationUrl = domain + "/api/auth/verify?token=" + token;

        int date = LocalDate.now().getYear();

        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        // HTML content
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "   .container { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "   .header {padding: 10px; text-align: center; font-size: 20px; }" +
                "   .body { margin: 20px 0; font-size: 16px; color: #333; }" +
                "   .button { display: inline-block; padding: 10px 20px; font-size: 16px; " +
                "             background-color: #0047AB; color: white; text-decoration: none; border-radius: 5px; }" +
                "   .footer { margin-top: 20px; font-size: 12px; color: #888; text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "   <div class='container'>" +
                "       <div class='header'>Secure Echos</div>" +
                "       <div class='body'>" +
                "           <p>Hi user,</p>" +
                "           <p>Thank you for signing up. Please click the button below to verify your email address:</p>" +
                "           <p><a href='" + verificationUrl + "' class='button'>Verify Email</a></p>" +
                "           <p>Ignore if you haven't created account.</p>" +
                "       </div>" +
                "       <div class='footer'>© "+ date + " Secure Echos. All rights reserved.</br> Please do not reply this mail.</div>" +
                "   </div>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendAccountDeletionEmail(String to) {
        String subject = "Account Deletion Confirmation - Secure Echos";

        int date = LocalDate.now().getYear();
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        // HTML content
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "   .container { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "   .header {padding: 10px; text-align: center; font-size: 20px; }" +
                "   .body { margin: 20px 0; font-size: 16px; color: #333; }" +
                "   .footer { margin-top: 20px; font-size: 12px; color: #888; text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "   <div class='container'>" +
                "       <div class='header'>Secure Echos</div>" +
                "       <div class='body'>" +
                "           <p>Hi " + currentUserName + ",</p>" +
                "           <p>We're writing to confirm that your account has been successfully deleted from Secure Echos.</p>" +
                "           <p>We're sorry to see you go. If this was a mistake or you change your mind, you're always welcome to sign up again.</p>" +
                "           <p>Thank you for being a part of our community.</p>" +
                "       </div>" +
                "       <div class='footer'>© " + date + " Secure Echos. All rights reserved.</br> Please do not reply to this mail.</div>" +
                "   </div>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

