package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.configuration.TwilioConfig;
import com.neobis.mobiMarket.entity.SmsCode;
import com.neobis.mobiMarket.repository.TwilioRepo;
import com.neobis.mobiMarket.service.SmsCodeService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SmsCodeServiceImpl implements SmsCodeService {

    private final TwilioConfig twilioConfig;
    private final TwilioRepo twilioRepo;

    @PostConstruct
    public void setup() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }
    @Override
    public String sendConfirmationCodeToPhone(String mobileNumber) {
        String code = generateCode();
        SmsCode smsCode = new SmsCode();
        smsCode.setCode(code);
        smsCode.setPhone(mobileNumber);
        this.save(smsCode);
        PhoneNumber recipientNumber = new PhoneNumber(mobileNumber);
        PhoneNumber senderNumber = new PhoneNumber(twilioConfig.getTrialNumber());
        String msgBody = "Your verification code is : " + code;
        Message message = Message.creator(recipientNumber, senderNumber, msgBody).create();

        return "Verification code send to your mobile, please check your phone!";
    }

    @Override
    public String generateActivationCode() {
        int code = (int) (Math.random() * 100000);
        return String.format("%05d", code);
    }

    @Override
    public SmsCode save(SmsCode smsCode) {
        try {
            return twilioRepo.save(smsCode);
        }catch (DataAccessException e) {
            throw new RuntimeException("Could not save smsVerificationCode to the database", e);
        }
    }
}