package com.neobis.mobiMarket.repository;

import com.neobis.mobiMarket.entity.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationCodeRepo extends JpaRepository<ActivationCode, Long> {


    ActivationCode findByCode(String code);
}
