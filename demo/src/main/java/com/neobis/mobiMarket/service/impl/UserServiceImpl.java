package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.dto.UserDto;
import com.neobis.mobiMarket.entity.Product;
import com.neobis.mobiMarket.entity.Role;
import com.neobis.mobiMarket.entity.User;
import com.neobis.mobiMarket.exception.EmailNotFoundException;
import com.neobis.mobiMarket.exception.UserNotFoundException;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.CloudinaryService;
import com.neobis.mobiMarket.service.RoleService;
import com.neobis.mobiMarket.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
//@AllArgsConstructor
//@NoArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private  UserRepo userRepo;
    @Autowired
    private  RoleService roleService;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private  CloudinaryService cloudinaryService;

    public UserServiceImpl(UserRepo userRepo, RoleService roleService, BCryptPasswordEncoder passwordEncoder, CloudinaryService cloudinaryService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }

    public UserServiceImpl() {
    }

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
        log.info("Before saving user : {} ", user);
        userRepo.save(user);
        log.info("After saving user : {} ", user);
        return user;
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        try {
            return Optional.ofNullable(userRepo.findByPhone(phoneNumber));
        } catch (Exception e) {
            log.error("Error during user retrieval by phone number: {}", e.getMessage());
            throw new RuntimeException("Error during user retrieval by phone number", e);
        }
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public ResponseEntity<User> update(Long id, UserDto user, MultipartFile file) {
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

            if (file != null && !file.isEmpty()) {
                String profileImageUrl = cloudinaryService.uploadImage(file);
                userInDB.setProfileImage(profileImageUrl);
            }


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
    public List<Product> getFavoriteProducts(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Product> favoriteProducts = user.getFavorites();
            return ResponseEntity.ok(favoriteProducts).getBody();
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

    }
    @Override
    public void updateUserStatus(User user) {
        user.setEnabled(true);
        userRepo.save(user);
    }

    @Override
    public boolean isEmailVerified(String username) {
        Optional<User> optionalUser = userRepo.findByUsername(username);
        return optionalUser.map(User::isEnabled).orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUsername(username);
        return user.map(value -> new org.springframework.security.core.userdetails.User(
                        value.getUsername(), value.getPassword(), value.getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }
}
