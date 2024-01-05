package com.neobis.mobiMarket.controller;

import com.neobis.mobiMarket.dto.ProductDto;
import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.mapper.ProductMapper;
import com.neobis.mobiMarket.mapper.UserMapper;
import com.neobis.mobiMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @ModelAttribute UserDto userDto,
                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        User updatedUser = userService.update(id, userDto, file).getBody();
        if (updatedUser != null) {
            UserDto userDto1 = userMapper.entityToDto(updatedUser);
            return ResponseEntity.ok(userDto1);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ProductDto>> getLikedProducts(@PathVariable Long id) {
        List<ProductDto> likedProducts = productMapper.entitiesToDtos(userService.getFavoriteProducts(id));
        return ResponseEntity.ok().body(likedProducts);

    }


}
