package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.entity.ActivationCode;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.ActivationCodeService;
import com.neobis.mobiMarket.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {


    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final ActivationCodeService activationCodeService;

    public EmailService(JavaMailSender javaMailSender, UserRepo userRepo, ActivationCodeService activationCodeService) {
        this.javaMailSender = javaMailSender;
        this.userRepo = userRepo;
        this.activationCodeService = activationCodeService;
    }

    public String sendActivationEmail(String toEmail) {
        Optional<User> userOptional = userRepo.findByEmail(toEmail);
        String  activationCode = activationCodeService.generateActivationCode();;
        if (userOptional.isPresent()) {
            ActivationCode code = new ActivationCode();
            code.setEmail(toEmail);
            code.setCode(activationCode);
            code.setUser(userOptional.get());
            activationCodeService.save(code);
            try {
                sendCodeToMail(toEmail, activationCode);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send verification code",e);
            }
        }
        return "Activation code send successfully!";
    }
    private void sendCodeToMail(String email, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setTo(email);
        message.setSubject("Activate your account");
        message.setText("Your activation code below:\n\n" + code);
        javaMailSender.send(mimeMessage);
    }

}
