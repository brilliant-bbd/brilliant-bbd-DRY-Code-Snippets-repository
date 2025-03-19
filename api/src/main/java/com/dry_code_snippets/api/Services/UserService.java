package com.dry_code_snippets.api.Services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
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

    private static final String CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    private static final String CLIENT_URL = System.getenv("GOOGLE_CLIENT_URL");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(String authCode) {
        String jwt = null;
        try {
            jwt = getJwtToken(authCode);
        } catch (IOException e) {
            return null;
        }
        return jwt;
    }

    private static String getJwtToken(String authCode) throws IOException {
        String tokenRequest = "code=" + authCode +
                "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET +
                "&redirect_uri=" + CLIENT_URL + "&grant_type=authorization_code";

        HttpURLConnection tokenConnection = (HttpURLConnection) URI.create("https://oauth2.googleapis.com/token")
                .toURL().openConnection();
        tokenConnection.setRequestMethod("POST");
        tokenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        tokenConnection.setDoOutput(true);

        OutputStream postOutputStream = tokenConnection.getOutputStream();
        postOutputStream.write(tokenRequest.getBytes());

        InputStream inputStream = tokenConnection.getInputStream();

        String tokenObject = new String(inputStream.readAllBytes());
        Matcher matcher = Pattern.compile("\"id_token\": \"([^\"]*)").matcher(tokenObject);
        if (matcher.find()) {
            // Matches the JWT Token
            return matcher.group(1);
        } else {
            return null;
        }
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
