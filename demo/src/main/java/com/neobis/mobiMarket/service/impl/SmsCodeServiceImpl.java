package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.configuration.TwilioConfig;
import com.neobis.mobiMarket.entity.SmsCode;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.repository.TwilioRepo;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.SmsCodeService;
import com.neobis.mobiMarket.service.UserService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class SmsCodeServiceImpl implements SmsCodeService {

    private final TwilioConfig twilioConfig;
    private final TwilioRepo twilioRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(SmsCodeServiceImpl.class);
    @PostConstruct
    public void setup() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }
//    @Override
//    public String sendConfirmationCodeToPhone(String mobileNumber) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//        logger.info("Type of principal in SmsCodeService.save(): " + principal.getClass().getName());
//
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
//            if (userOptional.isEmpty()) {
//                throw new RuntimeException("User not found with username: " + userDetails.getUsername() + ", please sign up! ");
//            }
//            String code = generateActivationCode();
//            SmsCode smsCode = new SmsCode();
//            smsCode.setCode(code);
//            smsCode.setPhone(mobileNumber);
//            User user = userOptional.get();
//            smsCode.setUser(user);
//            this.save(smsCode);
//            PhoneNumber recipientNumber = new PhoneNumber(mobileNumber);
//            PhoneNumber senderNumber = new PhoneNumber(twilioConfig.getTrialNumber());
//            String msgBody = "Your verification code is : " + code;
//            Message message = Message.creator(recipientNumber, senderNumber, msgBody).create();
//
//            return "Verification code send to your mobile, please check your phone!";
//        }else {
//        return "Unauthorized access or invalid principal type";
//        }
//    }

//    @Override
//    public String sendConfirmationCodeToPhone(String mobileNumber) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("User is not authenticated");
//        }

//        String username = authentication.getName();
//
//        try {
//            Optional<User> userOptional = userService.findByUsername(username);
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//
//                if (!userService.isPhoneNumberVerified(username)) {
//                    throw new RuntimeException("Please verify your account to enable!");
//                }
//
//                String code = generateActivationCode();
//                SmsCode smsCode = new SmsCode();
//                smsCode.setCode(code);
//                smsCode.setPhone(mobileNumber);
//                smsCode.setUser(user);
//                this.save(smsCode);
//
//                PhoneNumber recipientNumber = new PhoneNumber(mobileNumber);
//                PhoneNumber senderNumber = new PhoneNumber(twilioConfig.getTrialNumber());
//                String msgBody = "Your verification code is : " + code;
//                Message message = Message.creator(recipientNumber, senderNumber, msgBody).create();
//
//                return "Verification code sent to your mobile, please check your phone!";
//            } else {
//                throw new RuntimeException("User not found for username: " + username);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error during verification: " + e.getMessage());
//        }
//    }

//    @Override
//    public String sendConfirmationCodeToPhone(String mobileNumber) {
//        String code = generateActivationCode();
//        SmsCode smsCode = new SmsCode();
//        smsCode.setCode(code);
//        smsCode.setPhone(mobileNumber);
//        User user = userRepo.findByPhone(mobileNumber);
//        smsCode.setUser(user);
//        this.save(smsCode);
//        PhoneNumber recipientNumber = new PhoneNumber(mobileNumber);
//        PhoneNumber senderNumber = new PhoneNumber(twilioConfig.getTrialNumber());
//        String msgBody = "Your verification code is : " + code;
//        Message message = Message.creator(recipientNumber, senderNumber, msgBody).create();
//
//        return "Verification code send to your mobile, please check your phone!";
//    }

//    @Override
//    public String sendConfirmationCodeToPhone(String mobileNumber) {
//        Optional<User> userOptional = userService.findByPhoneNumber(mobileNumber);
//        String code = generateActivationCode();
//        SmsCode smsCode = new SmsCode();
//        smsCode.setCode(code);
//        smsCode.setPhone(mobileNumber);
//        this.save(smsCode);
//        PhoneNumber recipientNumber = new PhoneNumber(mobileNumber);
//        PhoneNumber senderNumber = new PhoneNumber(twilioConfig.getTrialNumber());
//        String msgBody = "Your verification code is : " + code;
//        Message message = Message.creator(recipientNumber, senderNumber, msgBody).create();
//
//        return "Verification code send to your mobile, please check your phone!";
//    }

    @Override
    public String sendConfirmationCodeToPhone(String phoneNumber) {
        try {
            Optional<User> userOptional = userService.findByPhoneNumber(phoneNumber);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                String code = generateActivationCode();
                SmsCode smsCode = new SmsCode();
                smsCode.setCode(code);
                smsCode.setPhone(phoneNumber);
                smsCode.setUser(user);
                this.save(smsCode);

                PhoneNumber recipientNumber = new PhoneNumber(phoneNumber);
                PhoneNumber senderNumber = new PhoneNumber(twilioConfig.getTrialNumber());
                String msgBody = "Your verification code is: " + code;
                Message.creator(recipientNumber, senderNumber, msgBody).create();

                return "Verification code sent to your mobile, please check your phone!";
            } else {
                return "User not found for phone number: " + phoneNumber;
            }
        } catch (Exception e) {
            return "Error during verification: " + e.getMessage();
        }
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

    @Override
    public String validateCode(String code) {
        try {
            SmsCode smsCode = twilioRepo.findByCode(code);
            if (smsCode != null && code.equals(smsCode.getCode())) {
                User user = userRepo.findByPhone(smsCode.getPhone());
                user.setEnabled(true);
                userRepo.save(user);
                return "Verification successful!";
            }else {
                return "Invalid confirmation code!";
            }
        } catch (Exception e) {
            return "Error during verification: " + e.getMessage();
        }

    }
}