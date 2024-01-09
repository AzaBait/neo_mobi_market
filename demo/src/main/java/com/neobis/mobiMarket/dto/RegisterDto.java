package com.neobis.mobiMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {

    private String username;
    private String phone;
    private String password;
}
