package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.service.SmsCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/smsCode")
public class SmsConfirmationController {

    private final SmsCodeService codeService;
    @PostMapping("/sendConfirmationCode")
    public String sendConfirmationCode(@RequestParam String phoneNumber) {
        return codeService.sendConfirmationCodeToPhone(phoneNumber);
    }
}
