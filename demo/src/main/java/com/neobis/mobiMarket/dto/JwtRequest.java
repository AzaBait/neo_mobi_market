package com.neobis.mobiMarket.dto;

import lombok.Data;

@Data
public class JwtRequest {

    private final String username;
    private final String password;
}
