package com.dry_code_snippets.api.Services;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    public void login() {
        userRepository.findByUserGuid(getClaim())
        .orElseGet(() -> userRepository.save(new User(getClaim())));
    }
    public UUID getClaim() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userGuid = null;
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();

            userGuid = convertStringToUUID(jwt.getClaim("sub"));
        }
        return userGuid;
    }
    
    private static UUID convertStringToUUID(String largeNumberString) {
        if (largeNumberString == null || largeNumberString.length() > 32) {
            throw new IllegalArgumentException("String is too long or invalid for a valid UUID.");
        }

        while (largeNumberString.length() < 32) {
            largeNumberString = "0" + largeNumberString;
        }

        String highBitsString = largeNumberString.substring(0, 16);
        String lowBitsString = largeNumberString.substring(16);

        long highBits = Long.parseLong(highBitsString, 16);
        long lowBits = Long.parseLong(lowBitsString, 16);

        return new UUID(highBits, lowBits);
    }
}
