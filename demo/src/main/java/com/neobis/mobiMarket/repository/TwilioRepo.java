package com.neobis.mobiMarket.repository;

import com.neobis.mobiMarket.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwilioRepo extends JpaRepository<SmsCode, Long> {
}
