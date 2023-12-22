package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.mapper.UserMapper;
import com.neobis.mobiMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        User updatedUser = userService.update(id, userDto).getBody();
        if (updatedUser != null) {
            UserDto userDto1 = userMapper.entityToDto(updatedUser);
            return ResponseEntity.ok(userDto1);
        }else {
            return ResponseEntity.notFound().build();
        }
    }


}
