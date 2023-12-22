package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.Role;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.exception.EmailNotFoundException;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.RoleService;
import com.neobis.mobiMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with username " + username + " not found!");
        }
        return (userOptional);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> userWithEmail = userRepo.findByEmail(email);
        if (userWithEmail.isEmpty()) {
            throw new EmailNotFoundException("User with email " + email + " not found!");
        }
        return (userWithEmail);
    }

    @Override
    public User save(User user) {
        Optional<User> userWithUsername = userRepo.findByUsername(user.getUsername());
        Optional<User> userWithEmail = userRepo.findByEmail(user.getEmail());
        if (userWithUsername.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This username " + user.getUsername() + " is already exists!");
        }
        if (userWithEmail.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "This email " + user.getEmail() + " is already exists!");
        }
        Role userRole;
        try {
            userRole = roleService.findByName("ROLE_USER").orElseThrow(
                    () -> new RoleNotFoundException("Role 'USER' not found!"));
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return user;
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<User> update(Long id, UserDto user) {
        User userInDB;
        try {
            userInDB = userRepo.findById(id).orElseThrow(()
                    -> new IllegalStateException("User with id " + id + " not found!"));

            userInDB.setUsername(user.getUsername());
            userInDB.setFirstName(user.getFirstName());
            userInDB.setLastName(user.getLastName());
            userInDB.setPassword(user.getPassword());
            userInDB.setEmail(user.getEmail());
            userInDB.setPhone(user.getPhone());
            userInDB.setProfileImage(user.getProfileImage());

            userRepo.save(userInDB);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok(userInDB);
    }


    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        return user.map(value -> new org.springframework.security.core.userdetails.User(
                value.getUsername(), value.getPassword(), value.getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }
}
