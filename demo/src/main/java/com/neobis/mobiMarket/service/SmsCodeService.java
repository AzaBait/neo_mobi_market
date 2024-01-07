package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.entity.SmsCode;

public interface SmsCodeService {
    String sendConfirmationCodeToPhone(String phoneNumber);
    String generateActivationCode();
    SmsCode save (SmsCode smsCode);



}
