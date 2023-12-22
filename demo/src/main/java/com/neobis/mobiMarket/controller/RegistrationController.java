package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.dto.RegisterDto;
import com.neobis.mobiMarket.mapper.UserMapper;
import com.neobis.mobiMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final UserService userService;
    private final UserMapper userMapper;
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@Validated @RequestBody RegisterDto registerDto) {
        userService.save(userMapper.registerDtoToEntity(registerDto));
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
}
