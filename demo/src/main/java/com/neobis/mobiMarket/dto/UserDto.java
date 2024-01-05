package com.neobis.mobiMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profileImage;
    private String password;

}
