package com.dry_code_snippets.api.Shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

public class Shared {

    
    public static UUID getClaim() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userGuid = null;
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();

            userGuid = convertStringToUUID(jwt.getClaim("sub"));
        }
        return userGuid;
    }

    public static UUID convertStringToUUID(String largeNumberString) {
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
