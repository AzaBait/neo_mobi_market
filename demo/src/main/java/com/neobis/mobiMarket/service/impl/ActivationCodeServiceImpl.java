package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.entity.ActivationCode;
import com.neobis.mobiMarket.exception.VerificationCodeNotFoundException;
import com.neobis.mobiMarket.repository.ActivationCodeRepo;
import com.neobis.mobiMarket.service.ActivationCodeService;
import com.neobis.mobiMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ActivationCodeServiceImpl implements ActivationCodeService {

    private final ActivationCodeRepo activationCodeRepo;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ActivationCodeServiceImpl.class);

    @Override
    public String generateActivationCode() {
        int code = (int) (Math.random() * 100000);
        return String.format("%05d", code);
    }

    @Override
    public ActivationCode save(ActivationCode activationCode) {
        try {
            return activationCodeRepo.save(activationCode);
        } catch (DataAccessException e) {
            throw new RuntimeException("Could not save VerificationCode to the database", e);
        }
    }

    @Override
    public String validateCode(String code) {
        try {
            ActivationCode activationCode = activationCodeRepo.findByCode(code);
            if (activationCode != null && code.equals(activationCode.getCode())) {
                return userService.findByEmail(activationCode.getEmail())
                        .map(user -> {
//                            user.setEnabled(true);
                            userService.updateUserStatus(user);
                            return "Verification successful!";
                        })
                        .orElse("User not found for the provided email!");
            } else {
                return "Invalid confirmation code!";
            }
        } catch (Exception e) {
            return "Error during verification: " + e.getMessage();
        }
    }

    @Override
    public ActivationCode findByCode(String code) {
        try {
            return activationCodeRepo.findByCode(code);
        } catch (VerificationCodeNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
