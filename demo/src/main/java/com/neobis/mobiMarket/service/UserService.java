package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> getById(Long id);

    ResponseEntity<User> update(Long id, UserDto user, MultipartFile file);

    ResponseEntity<List<User>> getAllUsers();

    ResponseEntity<String> deleteUser(Long id);

    List<Product> getFavoriteProducts(Long userId);

    boolean isPhoneNumberVerified(String username);

}
