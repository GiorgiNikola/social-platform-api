package com.socialplatformapi.service;

import com.socialplatformapi.exception.auth.MissingSessionTokenException;
import com.socialplatformapi.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorizationService {
    private final SessionService sessionService;

    public User getLoggedInUser(HttpServletRequest request) {
        String token = request.getHeader("X-Session-Token");

        if (token == null) {
            throw new MissingSessionTokenException();
        }

        return sessionService.getUserByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired session token"));
    }
}
