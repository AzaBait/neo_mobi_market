package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
    Optional<User> getById(Long id);
    ResponseEntity<User> update(Long id, UserDto user);
    ResponseEntity<List<User>> getAllUsers();
    ResponseEntity<String> deleteUser(Long id);

}
