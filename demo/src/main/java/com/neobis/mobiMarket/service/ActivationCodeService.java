package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.entity.ActivationCode;

public interface ActivationCodeService {
    String generateActivationCode();
    ActivationCode save (ActivationCode activationCode);
    String validateCode(String code);
    ActivationCode findByCode(String code);

}
