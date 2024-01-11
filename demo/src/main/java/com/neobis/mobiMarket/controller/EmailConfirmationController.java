package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.service.ActivationCodeService;
import com.neobis.mobiMarket.service.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailConfirmationController {

    private final ActivationCodeService codeService;
    private final EmailService emailService;
    @PostMapping("/sendConfirmationCode")
    public String sendConfirmationCode(@RequestParam String email,@RequestParam String username) {
        try {
            return emailService.sendActivationEmail(email, username);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @PostMapping("/verifyCode")
    public ResponseEntity<String> confirmCode(@RequestParam String code) {
        String result = codeService.validateCode(code);
        return ResponseEntity.ok(result);
    }
}