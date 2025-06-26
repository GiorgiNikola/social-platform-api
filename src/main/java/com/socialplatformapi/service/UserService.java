package com.socialplatformapi.service;

import com.socialplatformapi.dto.auth.UserLoginRequest;
import com.socialplatformapi.dto.register.UserRegisterRequest;
import com.socialplatformapi.dto.user.UserSummary;
import com.socialplatformapi.exception.user.EmailAlreadyExistsException;
import com.socialplatformapi.exception.auth.InvalidCredentialsException;
import com.socialplatformapi.exception.user.UsernameAlreadyExistsException;
import com.socialplatformapi.model.User;
import com.socialplatformapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public void registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setUsername(request.getUsername())
                .setBirthDate(request.getBirthDate())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public String login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return sessionService.createSession(user);
    }

    public List<UserSummary> getAllUserNames(Pageable pageable) {
        return userRepository.findAllProjectedBy(pageable);
    }
}
