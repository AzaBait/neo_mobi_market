package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.dto.JwtRequest;

public interface AuthenticationService {

    String authenticateAndGetToken(JwtRequest jwtRequest) throws Exception;
}
