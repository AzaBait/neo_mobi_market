package com.neobis.mobiMarket.dto;

import com.neobis.mobiMarket.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

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
