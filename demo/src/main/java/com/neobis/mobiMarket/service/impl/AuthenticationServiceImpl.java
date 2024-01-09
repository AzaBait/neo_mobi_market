package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.configuration.security.jwt.JwtTokenUtil;
import com.neobis.mobiMarket.dto.JwtRequest;
import com.neobis.mobiMarket.repository.UserRepo;
import com.neobis.mobiMarket.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final UserRepo userRepo;

    @Override
    public String authenticateAndGetToken(JwtRequest jwtRequest) {
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
            final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
            if (!userService.isPhoneNumberVerified(jwtRequest.getUsername())){
                throw new RuntimeException("Please verify your account to enable!");
            }
            return jwtTokenUtil.generateToken(userDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("User disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }
}
