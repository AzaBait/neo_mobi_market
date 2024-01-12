package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.entity.ActivationCode;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.service.ActivationCodeService;
import com.neobis.mobiMarket.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Setter
@Getter
@Slf4j
@Service
public class EmailService {

    @Autowired
    private  JavaMailSender javaMailSender;
    @Autowired
    private  ActivationCodeService activationCodeService;
    @Autowired
    private UserService userService;

    public EmailService(JavaMailSender javaMailSender, UserService userService, ActivationCodeService activationCodeService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
        this.activationCodeService = activationCodeService;
        log.info("lsjkcklcwlvbuweib");
    }

    public EmailService() {
    }

    public String sendActivationEmail(String toEmail) {
        Optional<User> userOptional = userService.findByEmail(toEmail);
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
