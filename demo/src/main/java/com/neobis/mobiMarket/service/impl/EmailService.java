package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.entity.ActivationCode;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.ActivationCodeService;
import com.neobis.mobiMarket.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailService {
    @Value("")
    private String from;

    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final ActivationCodeService activationCodeService;

    public String sendActivationEmail(String toEmail) {
        Optional<User> userOptional = userRepo.findByEmail(toEmail);
        String  activationCode = activationCodeService.generateActivationCode();;
        if (userOptional.isPresent()) {
            ActivationCode code = new ActivationCode();
            code.setEmail(toEmail);
            code.setCode(activationCode);
            code.setUser(userOptional.get());
            activationCodeService.save(code);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("{gmail_username}");
        message.setTo(toEmail);
        message.setSubject("Activate your account");
        message.setText("Your activation code below:\n\n" + activationCode);
        javaMailSender.send(message);
        return "Activation code send successfully!";
    }

}
